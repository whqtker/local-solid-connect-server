package com.example.solidconnection.auth.token;

import static com.example.solidconnection.auth.domain.TokenType.BLACKLIST;

import com.example.solidconnection.auth.service.AccessToken;
import com.example.solidconnection.security.filter.BlacklistChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

// 사용자 로그아웃 시 만료되지 않은 access token을 블랙리스트로 등록하여 해당 토큰을 사용한 후속 요청을 차단한다.
@Component
@RequiredArgsConstructor
public class TokenBlackListService implements BlacklistChecker {

    private static final String SIGN_OUT_VALUE = "signOut";

    private final RedisTemplate<String, String> redisTemplate;

    /*
     * 액세스 토큰을 블랙리스트에 저장한다.
     * - key = BLACKLIST:{accessToken}
     * - value = {SIGN_OUT_VALUE} -> key 의 존재만 확인하므로, value 에는 무슨 값이 들어가도 상관없다.
     * */
    // 로그아웃 시 해당 메서드가 호출된다.
    public void addToBlacklist(AccessToken accessToken) {
        String blackListKey = BLACKLIST.addPrefix(accessToken.token());
        redisTemplate.opsForValue().set(blackListKey, SIGN_OUT_VALUE);
    }

    @Override
    public boolean isTokenBlacklisted(String accessToken) {
        String blackListTokenKey = BLACKLIST.addPrefix(accessToken);
        return redisTemplate.hasKey(blackListTokenKey);
    }

    // Q. TTL 설정 안 해도 되는지?
}
