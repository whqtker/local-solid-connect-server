package com.example.solidconnection.auth.client;

import static com.example.solidconnection.common.exception.ErrorCode.APPLE_AUTHORIZATION_FAILED;
import static com.example.solidconnection.common.exception.ErrorCode.INVALID_APPLE_ID_TOKEN;

import com.example.solidconnection.auth.client.config.AppleOAuthClientProperties;
import com.example.solidconnection.auth.dto.oauth.AppleTokenDto;
import com.example.solidconnection.auth.dto.oauth.AppleUserInfoDto;
import com.example.solidconnection.common.exception.CustomException;
import io.jsonwebtoken.Jwts;
import java.security.PublicKey;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/*
 * 애플 인증을 위한 OAuth2 클라이언트
 * https://developer.apple.com/documentation/signinwithapplerestapi/generate_and_validate_tokens
 * */
// 애플로부터 받은 인증 코드를 통해 사용자 정보가 담긴 토큰을 받고 이를 검증한 후 정보를 추출한다.
@Component
@RequiredArgsConstructor
public class AppleOAuthClient {

    // 외부 API와 통신을 하기 위한 스프링 도구
    private final RestTemplate restTemplate;

    private final AppleOAuthClientProperties properties;
    private final AppleOAuthClientSecretProvider clientSecretProvider;
    private final ApplePublicKeyProvider publicKeyProvider;

    public AppleUserInfoDto processOAuth(String code) {
        String idToken = requestIdToken(code);
        PublicKey applePublicKey = publicKeyProvider.getApplePublicKey(idToken);
        return new AppleUserInfoDto(parseEmailFromToken(applePublicKey, idToken));
    }

    // 토큰 발급 엔드포인트로 HTTP 요청 보냄
    public String requestIdToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // Apple이 요구하는 형식
        MultiValueMap<String, String> formData = buildFormData(code); // 요청 바디에 들어갈 파라미터를 구성

        try {
            // exchange: 요청 메시지를 만들고 전송하여 응답을 받는 메서드
            ResponseEntity<AppleTokenDto> response = restTemplate.exchange(
                    properties.tokenUrl(),
                    HttpMethod.POST,
                    new HttpEntity<>(formData, headers),
                    AppleTokenDto.class // JSON 객체를 AppleTokenDto로 역직렬화
            );
            // 성공적으로 응답을 받으면 응답 바디에서 idToken을 추출하여 리턴
            return Objects.requireNonNull(response.getBody()).idToken();
        } catch (Exception e) {
            throw new CustomException(APPLE_AUTHORIZATION_FAILED, e.getMessage());
        }
    }

    // 토큰 요청에 필요한 파라미터 구성
    private MultiValueMap<String, String> buildFormData(String code) {
        // MultiValueMap: key 하나에 여러 value, 개념적으로 Map<String, List<String>>와 동일함.
        // LinkedMultiValueMap: MultiValueMap의 구현체로, 순서가 보장됨.
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("client_id", properties.clientId());
        formData.add("client_secret", clientSecretProvider.generateClientSecret());
        formData.add("code", code); // 로그인 후 애플로부터 받은 일회용 인증용 코드
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", properties.redirectUrl());
        return formData;

        // Q. 하나의 key에 하나의 value가 매핑되고 있는데 왜 MultiValueMap을 사용하는가 ?
        // A. RestTemplate과 데이터를 주고받기 위한 사실상 공식 자료구조임.
        //    또한 HTTP 폼 데이터는 하나의 키에 여러 값을 가질 수 있음, 향후 변경에 유연하게 대처 가능
    }

    private String parseEmailFromToken(PublicKey applePublicKey, String idToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(applePublicKey) // Apple의 공개 키로 서명을 검증하도록 설정
                    .parseClaimsJws(idToken) // 서명 검증 수행, 성공 시 토큰 내용을 담은 객체 리턴
                    .getBody() // 토큰의 payload(claims) 가져옴
                    .get("email", String.class); // 이메일 추출
        } catch (Exception e) {
            throw new CustomException(INVALID_APPLE_ID_TOKEN);
        }
    }
}
