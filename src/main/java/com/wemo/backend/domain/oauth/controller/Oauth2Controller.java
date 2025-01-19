package com.wemo.backend.domain.oauth.controller;

import com.wemo.backend.domain.oauth.service.Oauth2Service;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Oauth2")
@RestController
@RequestMapping("/login/oauth2/callback")
@RequiredArgsConstructor
public class Oauth2Controller {

    private final Oauth2Service oauth2Service;

    @GetMapping("/kakao")
    public ResponseEntity<SuccessResponse<Void>> kakaoLogin(HttpServletRequest request, HttpServletResponse response) {
        // 인가 코드 가져오기
        String code = request.getParameter("code");
        // 카카오 accessToken 요청
        String kakaoAccessToken = oauth2Service.getKakaoAccessToken(code);
        // 로그인 처리 및 응답 반환
        return oauth2Service.kakaoLogin(kakaoAccessToken, response);
    }

    @GetMapping("/google")
    public ResponseEntity<SuccessResponse<Void>> googleLogin(HttpServletRequest request, HttpServletResponse response) {
        // 인가 코드 가져오기
        String code = request.getParameter("code");
        // 구글 accessToken 요청
        String googleAccessToken = oauth2Service.getGoogleAccessToken(code);
        // 로그인 처리 및 응답 반환
        return oauth2Service.googleLogin(googleAccessToken, response);
    }

    @GetMapping("/naver")
    public ResponseEntity<SuccessResponse<Void>> naverLogin(HttpServletRequest request, HttpServletResponse response) {
        // 인가 코드 가져오기
        String code = request.getParameter("code");
        // 네이버 accessToken 요청
        String naverAccessToken = oauth2Service.getNaverAccessToken(code);
        // 로그인 처리 및 응답 반환
        return oauth2Service.naverLogin(naverAccessToken, response);
    }

}
