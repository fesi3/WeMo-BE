package com.wemo.backend.domain.auth.token.service;

import com.wemo.backend.domain.auth.token.entity.RefreshToken;
import com.wemo.backend.domain.auth.token.repository.RefreshTokenRepository;
import com.wemo.backend.global.exception.CustomException;
import com.wemo.backend.global.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.wemo.backend.global.exception.ErrorCode.INVALID_REFRESH_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenManager {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final AccessTokenManager accessTokenManager;

    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private int REFRESH_TOKEN_EXPIRATION;

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * JWT refreshToken 생성 및 저장
     *
     * @param email 유저 아이디
     * @return 저장된 refreshToken
     */
    @Transactional
    public String saveRefreshToken(final String email) {

        String refreshToken = jwtTokenUtils.generateRefreshToken(email);

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByRefreshToken(email);

        if (existingToken.isPresent()) {
            log.info("existingToken : {}", existingToken.get());
            refreshTokenRepository.delete(existingToken.get());
            log.info("기존에 존재하는 refreshToken DB에서 삭제 완료");
        }

        redisTemplate.delete("email:" + email);
        log.info("기존에 존재하는 refreshToken DB에서 삭제 완료");

        // 새 refreshToken DB에 저장
        refreshTokenRepository.save(new RefreshToken(refreshToken, email));

        // Redis에서 기존에 발급된 refreshToken 있는지 확인
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String existingRedisToken = valueOperations.get(refreshToken);

        // 기존 refreshToken이 Redis에 존재하면 삭제
        if (existingRedisToken != null && !existingRedisToken.isEmpty()) {
            redisTemplate.delete(existingRedisToken);
            log.info("기존 refreshToken Redis에서 삭제 완료");
        }

        // 새 refreshToken을 Redis에 저장
        redisTemplate.opsForValue().set(refreshToken, email, REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);
        log.info("새로운 refreshToken Redis에 저장 완료");

        return refreshToken;
    }

    /**
     * refreshToken 유효성 검증
     *
     * @param refreshToken jwt 형태의 refreshToken
     * @return 검증이 완료된 refreshToken 객체 반환
     */
    public RefreshToken getRefreshToken(String refreshToken) {

        return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new CustomException(INVALID_REFRESH_TOKEN)
        );

    }

    /**
     * refreshToken 유효성 검증
     *
     * @param refreshToken 검증할 refreshToken
     * @return 유저 아이디 반환, 유효하지 않은 경우 null
     */
    public String validateRefreshToken(String refreshToken) {

        Optional<RefreshToken> token = refreshTokenRepository.findByRefreshToken(refreshToken);
        return token.map(RefreshToken::email).orElse(null);
    }

    /**
     * refreshToken 검증하고 새로운 토큰 생성 및 발급
     */
    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {

        log.info("accessToken 재발급 요청 메서드 호출");

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null)
            for (Cookie cookie : cookies) {

                if (cookie.getName().equals("Refresh-Token")) {
                    refreshToken = cookie.getValue();
                    log.info("토큰 재발급 요청의 refreshToken : {}", refreshToken);
                }
            }

        if (refreshToken == null || refreshToken.isEmpty()) {

            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_PROVIDED);
        }

        String email = validateRefreshToken(refreshToken);
        if (email == null) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = jwtTokenUtils.generateAccessToken(email);
        accessTokenManager.setAccessTokenInCookie(newAccessToken, request, response);

        log.info("토큰 생성 후 쿠키에 담기 완료!");
    }

    /**
     * refreshToken 을 쿠키에 저장
     *
     * @param refreshToken refreshToken 값
     */
    public void setRefreshTokenInCookie(String refreshToken, HttpServletRequest request, HttpServletResponse response) {

        // domain 을 동적으로 설정: 로컬에서는 "localhost", 배포 환경에서는 ".we-mo.store"
        String domain = request.getServerName().contains("localhost") ? "localhost" : ".we-mo.store";

        // 현재 시간 + 10분
        Date expiryDate = new Date(System.currentTimeMillis() + 10 * 60 * 1000);

        ResponseCookie cookie = ResponseCookie.from("Refresh-Token", refreshToken)
//                .maxAge(24 * 60 * 60)
                .domain(domain)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 쿠키 설정 후 만료 시간을 Expires 헤더에 추가
        String cookieWithExpiry = cookie + "; Expires=" + expiryDate;

        // 최종 쿠키를 응답에 추가
        response.addHeader(HttpHeaders.SET_COOKIE, cookieWithExpiry);
    }

    public void deleteRefreshTokenInCookie(HttpServletResponse response) {

        // "Refresh-Token" 쿠키 삭제를 위한 설정
        ResponseCookie cookie = ResponseCookie.from("Refresh-Token", "")
                .maxAge(0)  // 유효기간을 0으로 설정해서 삭제
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();

        // 쿠키 삭제를 위한 헤더 추가
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        log.info("Refresh-Token 쿠키 삭제 완료");
    }

    public boolean existsByEmail(String email) {

        return refreshTokenRepository.findByRefreshToken(email).isPresent();
    }

}
