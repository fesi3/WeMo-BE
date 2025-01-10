package com.wemo.backend.domain.auth.token.repository;

import com.wemo.backend.domain.auth.token.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepository {

    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private int REFRESH_TOKEN_EXPIRATION;

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * refreshToken 저장
     *
     * @param refreshToken 저장할 refreshToken
     */
    public void save(final RefreshToken refreshToken) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.refreshToken(), refreshToken.email());
        redisTemplate.expire(refreshToken.refreshToken(), REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS); // TTL 설정
    }

    /**
     * refreshToken 으로 유저 정보 찾기
     *
     * @param refreshToken 유저 정보를 찾을 refreshToken
     * @return refreshToken과 관련된 유저 정보를 담은 Optional refreshToken
     */
    public Optional<RefreshToken> findByEmail(final String refreshToken) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String email = valueOperations.get(refreshToken);

        return Optional.of(new RefreshToken(refreshToken, email));

    }

    /**
     * 만료된 refreshToken 삭제
     *
     * @param now 현재 시간
     */
    public void deleteExpiredTokens(LocalDateTime now) {

    }

    /**
     * 수동으로 refreshToken 삭제
     *
     * @param refreshToken refreshToken 객체
     */
    public void delete(RefreshToken refreshToken) {
        // Redis에서 해당 refreshToken 키 삭제
        redisTemplate.delete(refreshToken.refreshToken());
    }

}

