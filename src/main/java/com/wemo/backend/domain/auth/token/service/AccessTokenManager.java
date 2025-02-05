package com.wemo.backend.domain.auth.token.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
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
                .domain(".we-mo.shop") // 임시로 도메인 지정
                .sameSite("None")
                .secure(false) // 임시로 허용
                .httpOnly(true)
                .path("/")
                .build();

        // 쿠키 설정 후 만료 시간을 Expires 헤더에 추가
        String cookieWithExpiry = cookie + "; Expires=" + expiryDate;

        // 최종 쿠키를 응답에 추가
        response.addHeader(HttpHeaders.SET_COOKIE, cookieWithExpiry);
    }

    public void deleteAccessTokenInCookie(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("accessToken", null)
                .maxAge(0) // 쿠키 즉시 만료
                .sameSite("None") // 쿠키 SameSite 설정
                .secure(true) // HTTPS 환경에서만 전송
                .httpOnly(true) // JavaScript에서 접근 불가
                .path("/") // 모든 경로에서 유효
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString()); // 헤더에 쿠키 추가

        log.info("accessToken 쿠키 삭제 완료");

    }

}
