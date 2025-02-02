package com.wemo.backend.domain.auth;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wemo.backend.domain.auth.token.entity.RefreshToken;
import com.wemo.backend.domain.auth.token.service.JwtTokenUtils;
import com.wemo.backend.domain.auth.token.service.RefreshTokenManager;
import com.wemo.backend.domain.auth.token.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${JWT_ISSUER}")
    private String JWT_ISSUER;

    @Value("${JWT_SECRET_KEY}")
    private String JWT_SECRET;

    private final UserDetailsServiceImpl userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenManager refreshTokenManager;

    /**
     * JWT accessToken 기반으로 인증 정보 생성
     *
     * @param accessToken JWT accessToken
     * @return Authentication 객체 반환
     */
    public Authentication getAuthentication(String accessToken) {

        // 토큰에서 사용자 정보를 추출하여 UserDetails 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodeUsername(accessToken));
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());
    }

    /**
     * JWT accessToken 에서 사용자 아이디 추출
     *
     * @param accessToken JWT accessToken
     * @return 사용자 계정명 반환
     */
    public String decodeUsername(String accessToken) {

        // JWT 토큰 검증을 위한 JWTVerifier 생성
        JWTVerifier verifier = JWT
                .require(generateAlgorithm(JWT_SECRET))
                .build();
        DecodedJWT jwt = verifier.verify(accessToken);

        return jwt
                .getClaim(JwtTokenUtils.CLAIM_NAME)
                .asString();
    }

    /**
     * JWT 토큰 파싱 및 검증
     *
     * @param token JWT 토큰
     */
    public void parseToken(String token) {

        JWTVerifier verifier = JWT.require(generateAlgorithm(JWT_SECRET)).build();
        verifier.verify(token);
    }

    /**
     * HMAC256 알고리즘 생성
     *
     * @param secretKey 비밀키
     * @return HMAC256 알고리즘
     */
    private static Algorithm generateAlgorithm(String secretKey) {

        return Algorithm.HMAC256(secretKey.getBytes());
    }

    /**
     * accessToken 의 만료시간 초기화
     *
     * @param accessToken accessToken
     * @return 해당 토큰의 초기화된 만료시간 반환
     */
    public Long getExpiration(String accessToken) {

        DecodedJWT jwt;
        JWTVerifier verifier = JWT
                .require(generateAlgorithm(JWT_SECRET))
                .build();
        jwt = verifier.verify(accessToken);
        // accessToken 남은 시간
        Date expiration = jwt.getExpiresAt();
        log.info("accessToken 남은 시간 : {}", expiration);

        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public boolean validateAccessToken(String accessToken) {

        if (accessToken == null || accessToken.isEmpty()) {
            log.warn("accessToken이 존재하지 않습니다.");
            return false;
        }
        // 블랙리스트 확인
        if (tokenBlacklistService.isBlacklisted(accessToken)) {
            log.warn("블랙리스트에 등록된 토큰입니다.");
            return false;
        }
        try {
            parseToken(accessToken);
            log.info("유효한 accessToken입니다.");
            return true;
        } catch (TokenExpiredException e) {
            log.warn("accessToken이 만료되었습니다.");
        } catch (Exception e) {
            log.error("accessToken 검증 중 에러: {}", e.getMessage());
        }
        return false;
    }

    public boolean validateRefreshToken(String refreshToken) {

        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            log.warn("refreshToken이 존재하지 않습니다.");
            return false;
        }

        // JWT 형식 검증
        if (!isJwtFormatValid(refreshToken)) {
            log.warn("refreshToken이 JWT 형식이 아닙니다.");
            return false;
        }

        try {
            // JWT 파싱 및 만료 시간 확인
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(JWT_SECRET))
                    .withIssuer(JWT_ISSUER)
                    .build()
                    .verify(refreshToken);

            Date expiresAt = decodedJWT.getExpiresAt();
            if (expiresAt != null && expiresAt.before(new Date())) {  // 현재 시간보다 과거면 만료됨
                log.warn("refreshToken이 만료되었습니다. 만료 시간: {}", expiresAt);
                return false;
            }

            // refreshToken 조회
            RefreshToken foundRefreshToken = refreshTokenManager.getRefreshToken(refreshToken);

            // refreshToken 존재 여부 확인
            if (foundRefreshToken != null && foundRefreshToken.refreshToken() != null
                    && foundRefreshToken.email().equals(decodeUsername(refreshToken))) {
                log.info("유효한 refreshToken입니다.");
                return true;
            } else {
                log.warn("DB에 해당 refreshToken이 존재하지 않습니다.");
            }
        } catch (TokenExpiredException e) {
            log.warn("refreshToken이 만료되었습니다.: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("잘못된 refreshToken입니다.: {}", e.getMessage());
        } catch (Exception e) {
            log.error("refreshToken 검증 중 에러 발생: {}", e.getMessage());
        }
        return false;
    }

    /**
     * JWT 형식 검증 로직
     */
    private boolean isJwtFormatValid(String token) {

        try {
            String[] parts = token.split("\\.");
            return parts.length == 3; // 헤더, 페이로드, 서명
        } catch (Exception e) {
            log.error("JWT 형식 검증 중 에러 발생: {}", e.getMessage());
            return false;
        }
    }

}
