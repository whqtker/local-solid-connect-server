package com.example.solidconnection.application.domain;

import com.example.solidconnection.siteuser.domain.SiteUser;
import com.example.solidconnection.type.VerifyStatus;
import com.example.solidconnection.university.domain.UniversityInfoForApply;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import static com.example.solidconnection.type.VerifyStatus.PENDING;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@DynamicUpdate // 변경된 컬럼만 업데이트하는 SQL을 생성한다.
@DynamicInsert // null이 아닌 컬럼만 포함하는 SQL을 생성한다.
@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Gpa gpa;

    @Embedded
    private LanguageTest languageTest;

    @Setter
    @Column(columnDefinition = "varchar(50) not null default 'PENDING'")
    @Enumerated(EnumType.STRING)
    private VerifyStatus verifyStatus;

    @Column(length = 100)
    private String nicknameForApply;

    @Column(columnDefinition = "int not null default 1")
    private Integer updateCount;

    @Column(length = 50, nullable = false)
    private String term;

    @Column
    private boolean isDelete = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private UniversityInfoForApply firstChoiceUniversity;

    @ManyToOne(fetch = FetchType.LAZY)
    private UniversityInfoForApply secondChoiceUniversity;

    @ManyToOne(fetch = FetchType.LAZY)
    private UniversityInfoForApply thirdChoiceUniversity;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser siteUser;

    // 기본 정보만 생성하는 생성자
    public Application(
            SiteUser siteUser,
            Gpa gpa,
            LanguageTest languageTest,
            String term) {
        this.siteUser = siteUser;
        this.gpa = gpa;
        this.languageTest = languageTest;
        this.term = term;
        this.updateCount = 1;
        this.verifyStatus = PENDING;
    }

    // 전체 정보를 생성하는 생성자
    public Application(
            SiteUser siteUser,
            Gpa gpa,
            LanguageTest languageTest,
            String term,
            Integer updateCount,
            UniversityInfoForApply firstChoiceUniversity,
            UniversityInfoForApply secondChoiceUniversity,
            UniversityInfoForApply thirdChoiceUniversity,
            String nicknameForApply) {
        this.siteUser = siteUser;
        this.gpa = gpa;
        this.languageTest = languageTest;
        this.term = term;
        this.updateCount = updateCount;
        this.firstChoiceUniversity = firstChoiceUniversity;
        this.secondChoiceUniversity = secondChoiceUniversity;
        this.thirdChoiceUniversity = thirdChoiceUniversity;
        this.nicknameForApply = nicknameForApply;
        this.verifyStatus = PENDING;
    }

    // 새로운 지원서를 생성하는 생성자
    public Application(
            SiteUser siteUser,
            Gpa gpa,
            LanguageTest languageTest,
            String term,
            UniversityInfoForApply firstChoiceUniversity,
            UniversityInfoForApply secondChoiceUniversity,
            UniversityInfoForApply thirdChoiceUniversity,
            String nicknameForApply) {
        this.siteUser = siteUser;
        this.gpa = gpa;
        this.languageTest = languageTest;
        this.term = term;
        this.updateCount = 1;
        this.firstChoiceUniversity = firstChoiceUniversity;
        this.secondChoiceUniversity = secondChoiceUniversity;
        this.thirdChoiceUniversity = thirdChoiceUniversity;
        this.nicknameForApply = nicknameForApply;
        this.verifyStatus = PENDING;
    }

    // 지원서를 논리적으로 삭제 처리
    public void setIsDeleteTrue() {
        this.isDelete = true;
    }

    // 지원자가 선택한 대학 정보 업데이트
    public void updateUniversityChoice(
            UniversityInfoForApply firstChoiceUniversity,
            UniversityInfoForApply secondChoiceUniversity,
            UniversityInfoForApply thirdChoiceUniversity,
            String nicknameForApply) {
        if (this.firstChoiceUniversity != null) {
            this.updateCount++;
        }
        this.firstChoiceUniversity = firstChoiceUniversity;
        this.secondChoiceUniversity = secondChoiceUniversity;
        this.thirdChoiceUniversity = thirdChoiceUniversity;
        this.nicknameForApply = nicknameForApply;
    }
}
