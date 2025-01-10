package com.wemo.backend.domain.auth.token.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    @Value("${ACCESS_TOKEN_EXPIRATION}")
    private int ACCESS_TOKEN_EXPIRATION;

    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private int REFRESH_TOKEN_EXPIRATION;

    @Value("${CLAIM_USERNAME}")
    private String claimUsername; // 인스턴스 필드

    public static String CLAIM_NAME; // static 필드

    @PostConstruct
    public void init() {
        CLAIM_NAME = claimUsername; // 주입된 값을 static 필드에 할당
    }

    @Value("${JWT_ISSUER}")
    private String JWT_ISSUER;

    @Value("${JWT_SECRET_KEY}")
    private String JWT_SECRET;

    /**
     * accessToken 생성
     *
     * @param email 유저 아이디
     * @return 생성된 accessToken
     */
    public String generateAccessToken(String email) {

        return JWT.create()
                .withIssuer(JWT_ISSUER)
                .withClaim("type", "access")
                .withClaim(CLAIM_NAME, email)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .sign(generateAlgorithm(JWT_SECRET));
    }

    /**
     * refreshToken 생성
     *
     * @param email 유저 아이디
     * @return 생성된 refreshToken
     */
    public String generateRefreshToken(String email) {

        return JWT.create()
                .withIssuer(JWT_ISSUER)
                .withClaim("type", "refresh")
                .withClaim(CLAIM_NAME, email)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .sign(generateAlgorithm(JWT_SECRET));
    }

    /**
     * HMAC256 알고리즘 생성
     *
     * @param secretKey 비밀키
     * @return HMAC256 알고리즘
     */
    private Algorithm generateAlgorithm(String secretKey) {

        return Algorithm.HMAC256(secretKey.getBytes());
    }

}
