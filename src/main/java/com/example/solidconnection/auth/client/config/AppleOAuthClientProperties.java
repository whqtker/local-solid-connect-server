package com.example.solidconnection.auth.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// application-xxx.yml에 정의된 속성을 자바 객체 필드로 자동으로 바인딩해줌.
@ConfigurationProperties(prefix = "oauth.apple")
public record AppleOAuthClientProperties(
        String tokenUrl,
        String clientSecretAudienceUrl,
        String redirectUrl,
        String publicKeyUrl,
        String clientId,
        String teamId,
        String keyId,
        String secretKey
) {

}
