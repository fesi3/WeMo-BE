package com.wemo.backend.domain.oauth.service;

import com.wemo.backend.global.response.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface Oauth2Service {

    String getKakaoAccessToken(String code);

    ResponseEntity<SuccessResponse<Void>> kakaoLogin(String kakaoAccessToken, HttpServletResponse response);

    String getGoogleAccessToken(String code);

    ResponseEntity<SuccessResponse<Void>> googleLogin(String googleAccessToken, HttpServletResponse response);

    String getNaverAccessToken(String code);

    ResponseEntity<SuccessResponse<Void>> naverLogin(String naverAccessToken, HttpServletResponse response);

}
