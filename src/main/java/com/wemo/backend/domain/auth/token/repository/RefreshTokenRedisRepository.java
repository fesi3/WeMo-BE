package com.wemo.backend.domain.auth.token.repository;

import com.wemo.backend.domain.auth.token.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenRedisRepository {

    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private int REFRESH_TOKEN_EXPIRATION;

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * refreshToken 저장
     *
     * @param refreshToken 저장할 refreshToken
     */
    public void saveToRedis(final RefreshToken refreshToken) {

        log.info("refreshToken 저장메서드 호출");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // refreshToken을 key, 이메일을 value로 저장
        valueOperations.set(refreshToken.refreshToken(), refreshToken.email(), REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);

        // 저장 후 확인
        String storedValue = valueOperations.get(refreshToken.refreshToken());
        log.info("✅ 사용자 {}의 refreshToken Redis에 저장 완료", storedValue);
    }

    /**
     * refreshToken 으로 유저 정보 찾기
     *
     * @param refreshToken 유저 정보를 찾을 refreshToken
     * @return refreshToken과 관련된 유저 정보를 담은 Optional refreshToken
     */
    public Optional<RefreshToken> findByRefreshToken(final String refreshToken) {

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

        log.info("Redis에 삭제 요청한 refreshToken 객체 : {}", refreshToken);

        redisTemplate.delete(refreshToken.refreshToken());
    }

}

