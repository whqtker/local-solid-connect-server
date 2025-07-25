package com.example.solidconnection.auth.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// application-xxx.yml에 정의된 속성을 자바 객체 필드로 자동으로 바인딩해줌.
@ConfigurationProperties(prefix = "oauth.apple")
public record AppleOAuthClientProperties(
        String tokenUrl, // 토큰 발급 URL, 애플의 고정 URL
        String clientSecretAudienceUrl, // JWT 안에 aud 클레임을 포함해야 하는데, 거기에 들어가는 URL, 애플의 고정 URL
        String redirectUrl,
        String publicKeyUrl, // 애플의 공개 키 URL, 애플의 고정 URL
        String clientId, // Apple Developer에서 발급받은 서비스 ID
        String teamId, // 토큰의 발급자, iss 클레임에 포함됨, Apple Developer에서 발급받은 팀 ID
        String keyId, // JWT를 서명하기 위해 사용되는 비밀 키를 식별하는 ID, Apple Developer에서 발급받은 키 ID
        String secretKey // 실제 비밀 키
) {

}
