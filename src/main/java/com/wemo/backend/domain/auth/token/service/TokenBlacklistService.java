package com.wemo.backend.domain.auth.token.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String BLACKLIST_PREFIX = "blacklist:";

    /**
     * 토큰을 블랙리스트에 추가
     *
     * @param token        블랙리스트에 추가할 토큰
     * @param expirationMs 토큰 만료 시간 (ms)
     */
    public void addToBlacklist(String token, long expirationMs) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "blacklisted", expirationMs, TimeUnit.MILLISECONDS);
    }

    /**
     * 토큰이 블랙리스트에 있는지 확인
     *
     * @param token 확인할 토큰
     * @return 블랙리스트 포함 여부
     */
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }
}
