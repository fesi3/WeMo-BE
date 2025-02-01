package com.wemo.backend.domain.auth.token.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AccessTokenManager {

    /**
     * accessToken 을 쿠키에 저장
     *
     * @param accessToken accessToken 값
     */
    public void setAccessTokenInCookie(String accessToken, HttpServletResponse response) {

        // 현재 시간 + 5분
        Date expiryDate = new Date(System.currentTimeMillis() + 5 * 60 * 1000);

        // 쿠키 생성
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();

        // 쿠키 설정 후 만료 시간을 Expires 헤더에 추가
        String cookieWithExpiry = cookie + "; Expires=" + expiryDate;

        // 최종 쿠키를 응답에 추가
        response.addHeader(HttpHeaders.SET_COOKIE, cookieWithExpiry);
    }

}
