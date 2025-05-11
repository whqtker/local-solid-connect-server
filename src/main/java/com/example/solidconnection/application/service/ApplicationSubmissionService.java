package com.example.solidconnection.application.service;

import com.example.solidconnection.application.domain.Application;
import com.example.solidconnection.application.dto.ApplicationSubmissionResponse;
import com.example.solidconnection.application.dto.ApplyRequest;
import com.example.solidconnection.application.dto.UniversityChoiceRequest;
import com.example.solidconnection.application.repository.ApplicationRepository;
import com.example.solidconnection.cache.annotation.DefaultCacheOut;
import com.example.solidconnection.custom.exception.CustomException;
import com.example.solidconnection.score.domain.GpaScore;
import com.example.solidconnection.score.domain.LanguageTestScore;
import com.example.solidconnection.score.repository.GpaScoreRepository;
import com.example.solidconnection.score.repository.LanguageTestScoreRepository;
import com.example.solidconnection.siteuser.domain.SiteUser;
import com.example.solidconnection.type.VerifyStatus;
import com.example.solidconnection.university.domain.UniversityInfoForApply;
import com.example.solidconnection.university.repository.UniversityInfoForApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.solidconnection.custom.exception.ErrorCode.APPLY_UPDATE_LIMIT_EXCEED;
import static com.example.solidconnection.custom.exception.ErrorCode.GPA_SCORE_NOT_FOUND;
import static com.example.solidconnection.custom.exception.ErrorCode.INVALID_GPA_SCORE_STATUS;
import static com.example.solidconnection.custom.exception.ErrorCode.INVALID_LANGUAGE_TEST_SCORE;
import static com.example.solidconnection.custom.exception.ErrorCode.INVALID_LANGUAGE_TEST_SCORE_STATUS;

@RequiredArgsConstructor
@Service
public class ApplicationSubmissionService {

    public static final int APPLICATION_UPDATE_COUNT_LIMIT = 3;

    private final ApplicationRepository applicationRepository;
    private final UniversityInfoForApplyRepository universityInfoForApplyRepository;
    private final GpaScoreRepository gpaScoreRepository;
    private final LanguageTestScoreRepository languageTestScoreRepository;

    @Value("${university.term}")
    private String term;

    // 학점 및 어학성적이 모두 유효한 경우에만 지원서 등록이 가능하다.
    // 기존에 있던 status field 우선 APRROVED로 입력시킨다.
    @Transactional
    // todo: 임시로 새로운 신청 생성 시 기존 캐싱 데이터를 삭제한다. 추후 수정 필요
    // 새로운 지원서가 생성되면 캐시된 데이터를 삭제
    @DefaultCacheOut(
            key = {"applications:all"},
            cacheManager = "customCacheManager"
    )
    public ApplicationSubmissionResponse apply(SiteUser siteUser, ApplyRequest applyRequest) {
        // 대학 선택 정보, 학점, 어학성적 정보를 사용자로부터 가져온다.
        UniversityChoiceRequest universityChoiceRequest = applyRequest.universityChoiceRequest();
        GpaScore gpaScore = getValidGpaScore(siteUser, applyRequest.gpaScoreId());
        LanguageTestScore languageTestScore = getValidLanguageTestScore(siteUser, applyRequest.languageTestScoreId());

        // 사용자가 선택한 1, 2, 3지망 대학 정보를 가져온다. term 학기에 유효한 대학이어야 한다.
        // 1지망 대학은 필수, 2지망과 3지망은 선택사항
        UniversityInfoForApply firstChoiceUniversity = universityInfoForApplyRepository
                .getUniversityInfoForApplyByIdAndTerm(universityChoiceRequest.firstChoiceUniversityId(), term);
        UniversityInfoForApply secondChoiceUniversity = Optional.ofNullable(universityChoiceRequest.secondChoiceUniversityId())
                .map(id -> universityInfoForApplyRepository.getUniversityInfoForApplyByIdAndTerm(id, term))
                .orElse(null);
        UniversityInfoForApply thirdChoiceUniversity = Optional.ofNullable(universityChoiceRequest.thirdChoiceUniversityId())
                .map(id -> universityInfoForApplyRepository.getUniversityInfoForApplyByIdAndTerm(id, term))
                .orElse(null);

        // 사용자가 현재 학기에 이미 지원한 지원서가 있는지 확인
        Optional<Application> existingApplication = applicationRepository.findBySiteUserAndTerm(siteUser, term);
        int updateCount = existingApplication
                .map(application -> {
                    validateUpdateLimitNotExceed(application); // 지원서 업데이트 제한을 넘었는지 검증, 넘었다면 더 수행되지 않음
                    application.setIsDeleteTrue(); // 기존 지원서를 논리적으로 삭제 처리
                    return application.getUpdateCount() + 1; // 기존 지원서 업데이트 횟수에 1을 더함
                })
                .orElse(1); // 지원서가 없던 경우, 최초 지원

        // 새로운 지원서 객체 생성
        Application newApplication = new Application(siteUser, gpaScore.getGpa(), languageTestScore.getLanguageTest(),
                term, updateCount, firstChoiceUniversity, secondChoiceUniversity, thirdChoiceUniversity, getRandomNickname());
        newApplication.setVerifyStatus(VerifyStatus.APPROVED);
        applicationRepository.save(newApplication);
        return ApplicationSubmissionResponse.from(newApplication);
    }

    private GpaScore getValidGpaScore(SiteUser siteUser, Long gpaScoreId) {
        // 사용자가 등록한 학점인지 확인
        GpaScore gpaScore = gpaScoreRepository.findGpaScoreBySiteUserAndId(siteUser, gpaScoreId)
                .orElseThrow(() -> new CustomException(GPA_SCORE_NOT_FOUND));

        // 관리자로부터 승인된(APPROVED) 학점인지 확인
        if (gpaScore.getVerifyStatus() != VerifyStatus.APPROVED) {
            throw new CustomException(INVALID_GPA_SCORE_STATUS);
        }
        return gpaScore;
    }

    private LanguageTestScore getValidLanguageTestScore(SiteUser siteUser, Long languageTestScoreId) {
        // 사용자가 등록한 어학성적인지 확인
        LanguageTestScore languageTestScore = languageTestScoreRepository
                .findLanguageTestScoreBySiteUserAndId(siteUser, languageTestScoreId)
                .orElseThrow(() -> new CustomException(INVALID_LANGUAGE_TEST_SCORE));

        // 관리자로부터 승인된(APPROVED) 어학성적인지 확인
        if (languageTestScore.getVerifyStatus() != VerifyStatus.APPROVED) {
            throw new CustomException(INVALID_LANGUAGE_TEST_SCORE_STATUS);
        }
        return languageTestScore;
    }

    private String getRandomNickname() {
        // 초기 랜덤 닉네임 생성
        String randomNickname = NicknameCreator.createRandomNickname();

        // 고유한 닉네임이 아니라면 고유한 닉네임이 생성될 때까지 반복
        while (applicationRepository.existsByNicknameForApply(randomNickname)) {
            randomNickname = NicknameCreator.createRandomNickname();
        }
        return randomNickname;
    }

    // 지원서는 업데이트 제한이 있음
    private void validateUpdateLimitNotExceed(Application application) {
        if (application.getUpdateCount() >= APPLICATION_UPDATE_COUNT_LIMIT) {
            throw new CustomException(APPLY_UPDATE_LIMIT_EXCEED);
        }
    }
}
