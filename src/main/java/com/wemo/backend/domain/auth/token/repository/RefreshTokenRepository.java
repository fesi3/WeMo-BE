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

        log.info("refreshToken 저장메서드 호출");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        log.info("refreshToken.email() : {}", refreshToken.email());

        // refreshToken을 key, 이메일을 value로 저장
        valueOperations.set(refreshToken.refreshToken(), refreshToken.email(), REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);

        // 저장 후 확인
        String storedValue = valueOperations.get(refreshToken.refreshToken());
        log.info("✅ Redis 저장 완료 : {}", storedValue);
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

        redisTemplate.delete(refreshToken.refreshToken());
    }

    public Optional<RefreshToken> findByEmail(String email) {

        log.info("findByEmail 호출");

        // 이메일을 키로 사용하여 Redis에서 값 조회
        String refreshTokenValue = redisTemplate.opsForValue().get("email:" + email);

        log.info("email : {}", email);
        log.info("refreshTokenValue: {}", refreshTokenValue);

        // 값이 존재하는 경우, RefreshToken 객체로 변환하여 반환
        if (refreshTokenValue != null) {
            // refreshToken은 불변 객체이므로 생성자에 값만 전달하면 됩니다.
            return Optional.of(new RefreshToken(refreshTokenValue, email));
        }

        // 값이 존재하지 않으면 Optional.empty() 반환
        return Optional.empty();
    }

}

