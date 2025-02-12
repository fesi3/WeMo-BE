package com.wemo.backend.domain.auth.token.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class AccessTokenManager {

    @Value("${ACCESS_TOKEN_EXPIRATION}")
    private int ACCESS_TOKEN_EXPIRATION;

    /**
     * accessToken 을 쿠키에 저장
     *
     * @param accessToken accessToken 값
     */
    public void setAccessTokenInCookie(String accessToken, HttpServletRequest request, HttpServletResponse response) {

        Date expiryDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION);

        // domain 을 동적으로 설정: 로컬에서는 "localhost", 배포 환경에서는 "we-mo.store"
        String domain = request.getServerName().contains("localhost") ? "localhost" : "we-mo.store";

        // 로컬 환경에서는 Secure 비활성화, 배포 환경에서는 활성화
        boolean isLocal = request.getServerName().contains("localhost");
        boolean isSecure = !isLocal;

        // 쿠키 생성
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .domain(domain)           // 도메인 동적 설정
                .sameSite("None")         // SameSite 설정
                .secure(isSecure)         // secure 값 동적 설정
                .httpOnly(true)           // httpOnly 설정
                .path("/")                // 경로 설정
                .build();

        // 쿠키 설정 후 만료 시간을 Expires 헤더에 추가
        String cookieWithExpiry = cookie + "; Expires=" + expiryDate;

        // 최종 쿠키를 응답에 추가
        response.addHeader(HttpHeaders.SET_COOKIE, cookieWithExpiry);
    }

    public void deleteAccessTokenInCookie(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("accessToken", null)
                .maxAge(0)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        log.info("accessToken 쿠키 삭제 완료");

    }

}
