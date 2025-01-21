package com.wemo.backend.domain.auth.token.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenManager {

    /**
     * accessToken 을 쿠키에 저장
     *
     * @param accessToken accessToken 값
     */
    public void setAccessTokenInCookie(String accessToken, HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .maxAge(5 * 60)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}
