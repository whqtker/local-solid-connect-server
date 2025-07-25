package com.example.solidconnection.auth.token;

import static com.example.solidconnection.common.exception.ErrorCode.INVALID_TOKEN;

import com.example.solidconnection.auth.domain.TokenType;
import com.example.solidconnection.auth.service.TokenProvider;
import com.example.solidconnection.auth.token.config.JwtProperties;
import com.example.solidconnection.common.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {

    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public final String generateToken(String string, TokenType tokenType) {
        Claims claims = Jwts.claims().setSubject(string); // 누구에 대한 토큰인지 설정
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + tokenType.getExpireTime());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.secret())
                .compact();
    }

    // 토큰을 redis에 저장
    @Override
    public final String saveToken(String token, TokenType tokenType) {
        String subject = parseSubject(token);
        redisTemplate.opsForValue().set(
                tokenType.addPrefix(subject),
                token,
                tokenType.getExpireTime(), // TTL
                TimeUnit.MILLISECONDS
        );
        return token;
    }

    @Override
    public String parseSubject(String token) {
        return parseClaims(token).getSubject();
    }

    @Override
    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    // HMAC 알고리즘을 사용하였으므로 토큰 생성 사용한 비밀 키를 그대로 서명을 검증할 때 사용한다.
                    .setSigningKey(jwtProperties.secret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new CustomException(INVALID_TOKEN);
        }
    }
}
