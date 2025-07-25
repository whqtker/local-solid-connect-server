package com.example.solidconnection.auth.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoOAuthClientProperties(
        String tokenUrl,
        String userInfoUrl, // 사용자 정보를 조회하기 위한 URL
        String redirectUrl,
        String clientId
) {

}
