package com.example.solidconnection.auth.client;

import static com.example.solidconnection.common.exception.ErrorCode.INVALID_OR_EXPIRED_KAKAO_AUTH_CODE;
import static com.example.solidconnection.common.exception.ErrorCode.KAKAO_REDIRECT_URI_MISMATCH;
import static com.example.solidconnection.common.exception.ErrorCode.KAKAO_USER_INFO_FAIL;

import com.example.solidconnection.auth.client.config.KakaoOAuthClientProperties;
import com.example.solidconnection.auth.dto.oauth.KakaoTokenDto;
import com.example.solidconnection.auth.dto.oauth.KakaoUserInfoDto;
import com.example.solidconnection.common.exception.CustomException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/*
 * 카카오 인증을 위한 OAuth2 클라이언트
 * https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-code
 * https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token
 * https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
 * */
@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {

    private final RestTemplate restTemplate;
    private final KakaoOAuthClientProperties kakaoOAuthClientProperties;

    public KakaoUserInfoDto getUserInfo(String code) {
        String kakaoAccessToken = getKakaoAccessToken(code); // 인증 코드를 통해 access token 획득
        return getKakaoUserInfo(kakaoAccessToken); // access token을 사용하여 사용자 정보 조회
    }

    private String getKakaoAccessToken(String code) {
        try {
            ResponseEntity<KakaoTokenDto> response = restTemplate.exchange(
                    buildTokenUri(code),
                    HttpMethod.POST,
                    null,
                    KakaoTokenDto.class
            );
            return Objects.requireNonNull(response.getBody()).accessToken();
        } catch (Exception e) {
            if (e.getMessage().contains("KOE303")) {
                throw new CustomException(KAKAO_REDIRECT_URI_MISMATCH);
            }
            if (e.getMessage().contains("KOE320")) {
                throw new CustomException(INVALID_OR_EXPIRED_KAKAO_AUTH_CODE);
            }
            throw new CustomException(INVALID_OR_EXPIRED_KAKAO_AUTH_CODE);
        }
    }

    private String buildTokenUri(String code) {
        // 파라미터를 URL의 쿼리 스트링에 붙여서 보낸다.
        // 따라서 HttpEntity는 null로 전달된다.
        return UriComponentsBuilder.fromHttpUrl(kakaoOAuthClientProperties.tokenUrl())
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", kakaoOAuthClientProperties.clientId())
                .queryParam("redirect_uri", kakaoOAuthClientProperties.redirectUrl())
                .queryParam("code", code)
                .toUriString();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // 토큰을 Authorization 헤더에 추가

        // 사용자 정보를 조회하기 위한 GET 요청
        ResponseEntity<KakaoUserInfoDto> response = restTemplate.exchange(
                kakaoOAuthClientProperties.userInfoUrl(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                KakaoUserInfoDto.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new CustomException(KAKAO_USER_INFO_FAIL);
        }
    }
}
