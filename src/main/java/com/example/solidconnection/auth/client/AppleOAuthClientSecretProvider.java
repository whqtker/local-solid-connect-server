package com.example.solidconnection.auth.client;

import static com.example.solidconnection.common.exception.ErrorCode.FAILED_TO_READ_APPLE_PRIVATE_KEY;

import com.example.solidconnection.auth.client.config.AppleOAuthClientProperties;
import com.example.solidconnection.common.exception.CustomException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

/*
 * 애플 OAuth 에 필요한 클라이언트 시크릿은 매번 동적으로 생성해야 한다.
 * 클라이언트 시크릿은 애플 개발자 계정에서 발급받은 개인키(*.p8)를 사용하여 JWT 를 생성한다.
 * https://developer.apple.com/documentation/accountorganizationaldatasharing/creating-a-client-secret
 * */
@Component
@RequiredArgsConstructor
public class AppleOAuthClientSecretProvider {

    private static final String KEY_ID_HEADER = "kid";
    private static final long TOKEN_DURATION = 1000 * 60 * 10; // 10min

    private final AppleOAuthClientProperties appleOAuthClientProperties;
    private PrivateKey privateKey;

    // 빈 생성, 의존성 주입 후 해당 메서드가 한 번만 수행되도록 보장
    @PostConstruct
    private void initPrivateKey() {
        privateKey = loadPrivateKey();
    }

    // Apple이 요구하는 명세에 따라 JWT 생성
    public String generateClientSecret() {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TOKEN_DURATION);

        return Jwts.builder()
                .setHeaderParam("alg", "ES256") // 서명 알고리즘
                .setHeaderParam(KEY_ID_HEADER, appleOAuthClientProperties.keyId()) // kid 추가
                .setSubject(appleOAuthClientProperties.clientId())
                .setIssuer(appleOAuthClientProperties.teamId())
                .setAudience(appleOAuthClientProperties.clientSecretAudienceUrl())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.ES256, privateKey) // 실제 서명 단계
                .compact(); // header, payload, signature를 Base64URL로 인코딩, '.'으로 합쳐 JWT 생성
    }

    // 비밀 키를 PrivateKey 객체로 변환
    private PrivateKey loadPrivateKey() {
        try {
            String secretKey = appleOAuthClientProperties.secretKey();
            byte[] encoded = Base64.decodeBase64(secretKey); // 비밀 키를 디코딩하여 바이트 배열로 변환
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded); // PKCS#8 형식의 비밀 키로 래핑
            KeyFactory keyFactory = KeyFactory.getInstance("EC"); // 서명 알고리즘이 ES256이므로, EC용 키 팩토리 로드
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CustomException(FAILED_TO_READ_APPLE_PRIVATE_KEY);
        }
    }
}
