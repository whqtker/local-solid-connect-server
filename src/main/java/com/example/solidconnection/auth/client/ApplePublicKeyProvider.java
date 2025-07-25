package com.example.solidconnection.auth.client;

import static com.example.solidconnection.common.exception.ErrorCode.APPLE_ID_TOKEN_EXPIRED;
import static com.example.solidconnection.common.exception.ErrorCode.APPLE_PUBLIC_KEY_NOT_FOUND;
import static com.example.solidconnection.common.exception.ErrorCode.INVALID_APPLE_ID_TOKEN;
import static org.apache.tomcat.util.codec.binary.Base64.decodeBase64URLSafe;

import com.example.solidconnection.auth.client.config.AppleOAuthClientProperties;
import com.example.solidconnection.common.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/*
 * idToken 검증을 위해서 애플의 공개키를 가져온다.
 * - 애플 공개키는 주기적으로 바뀐다. 이를 효율적으로 관리하기 위해 캐싱한다.
 * - idToken 의 헤더에 있는 kid 값에 해당하는 키가 캐싱되어있으면 그것을 반환한다.
 * - 그렇지 않다면 공개키가 바뀌었다는 뜻이므로, JSON 형식의 공개키 목록을 받아오고 캐시를 갱신한다.
 * https://developer.apple.com/documentation/signinwithapplerestapi/fetch_apple_s_public_key_for_verifying_token_signature
 * */
@Component
@RequiredArgsConstructor
public class ApplePublicKeyProvider {

    private final AppleOAuthClientProperties properties;
    private final RestTemplate restTemplate;

    // 일반적인 HashMap은 Thread-safe하지 않다. 여러 쓰레드가 동시에 접근하는 경우 정합성 문제가 발생할 수 있음.
    // 따라서 동시성을 지원하는 ConcurrentHashMap을 사용한다.
    // 즉, 여러 쓰레드가 동시에 접근하여도 정합성을 보장한다.
    private final Map<String, PublicKey> applePublicKeyCache = new ConcurrentHashMap<>();

    public PublicKey getApplePublicKey(String idToken) {
        try {
            String kid = getKeyIdFromTokenHeader(idToken);
            if (applePublicKeyCache.containsKey(kid)) {
                return applePublicKeyCache.get(kid);
            }

            fetchApplePublicKeys();
            if (applePublicKeyCache.containsKey(kid)) {
                return applePublicKeyCache.get(kid);
            } else {
                throw new CustomException(APPLE_PUBLIC_KEY_NOT_FOUND);
            }
        } catch (ExpiredJwtException e) {
            throw new CustomException(APPLE_ID_TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new CustomException(INVALID_APPLE_ID_TOKEN);
        }
    }

    /*
     * idToken 은 JWS 이므로, 원칙적으로는 서명까지 검증되어야 parsing 이 가능하다
     * 서명을 검증하기 위해 PublicKey가 필요하고, PublicKey를 찾으려면 JWT 헤더의 kid가 필요하다.
     * 즉, 이 시점에서는 서명(=공개키)을 알 수 없으므로, JWT를 전제 파싱하는 것이 아닌
     * Jwt 를 직접 인코딩하여 헤더를 가져온다.
     * JWT는 Header, Payload, Signature가 '.'으로 구분된 문자열이다.
     * */
    private String getKeyIdFromTokenHeader(String idToken) throws JsonProcessingException {
        String[] jwtParts = idToken.split("\\.");
        if (jwtParts.length < 2) {
            throw new CustomException(INVALID_APPLE_ID_TOKEN);
        }

        // 첫 번째 부분은 헤더는 서명과 무관하게 Base64URL로 인코딩된 JSON 문자열이다.
        // 헤더에서 kid를 추출한다.
        String headerJson = new String(Base64.getUrlDecoder().decode(jwtParts[0]), StandardCharsets.UTF_8);
        return new ObjectMapper().readTree(headerJson).get("kid").asText();
    }

    // Apple 공개키 목록을 가져온다.
    private void fetchApplePublicKeys() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // GET 요청 전송
        ResponseEntity<String> response = restTemplate.getForEntity(properties.publicKeyUrl(), String.class);

        // 응답에서 keys라는 배열 추출, RSA 공캐 키에 사용할 n, e가 들어있음
        JsonNode jsonNode = objectMapper.readTree(response.getBody()).get("keys");

        // 기존 캐시 초기화
        applePublicKeyCache.clear();

        // jsonNode를 순회하며 공개 키를 만들고 kid를 키로 캐시에 저장
        for (JsonNode key : jsonNode) {
            applePublicKeyCache.put(key.get("kid").asText(), generatePublicKey(key));
        }
    }

    private PublicKey generatePublicKey(JsonNode key) throws Exception {
        // n, e를 key에서 추출, Base64URL로 인코딩되어 있으므로 디코딩한다.
        BigInteger modulus = new BigInteger(1, decodeBase64URLSafe(key.get("n").asText()));
        BigInteger exponent = new BigInteger(1, decodeBase64URLSafe(key.get("e").asText()));

        // n, e를 바탕으로 RSA 공개 키를 생성한다.
        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
