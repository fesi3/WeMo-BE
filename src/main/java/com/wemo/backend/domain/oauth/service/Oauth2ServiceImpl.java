package com.wemo.backend.domain.oauth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wemo.backend.domain.auth.token.service.AccessTokenManager;
import com.wemo.backend.domain.auth.token.service.JwtTokenUtils;
import com.wemo.backend.domain.auth.token.service.RefreshTokenManager;
import com.wemo.backend.domain.user.entity.LoginType;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import com.wemo.backend.domain.user.service.UserStore;
import com.wemo.backend.global.exception.CustomException;
import com.wemo.backend.global.exception.ErrorCode;
import com.wemo.backend.global.response.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2ServiceImpl implements Oauth2Service {

    private final WebClient webClient;
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenManager refreshTokenManager;
    private final UserReader userReader;
    private final UserStore userStore;
    private final AccessTokenManager accessTokenManager;

    @Value("${kakao.client.id}")
    private String kakaoClientId;
    @Value("${kakao.client.secret}")
    private String kakaoClientSecret;
    @Value("${kakao.client.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${google.client.id}")
    private String googleClientId;
    @Value("${google.client.secret}")
    private String googleClientSecret;
    @Value("${google.client.redirect-uri}")
    private String googleRedirectUri;

    @Value("${naver.client.id}")
    private String naverClientId;
    @Value("${naver.client.secret}")
    private String naverClientSecret;
    @Value("${naver.client.redirect-uri}")
    private String naverRedirectUri;

    @Override
    public String getKakaoAccessToken(String code) {

        return getAccessToken("https://kauth.kakao.com/oauth/token", code, kakaoClientId, kakaoClientSecret, kakaoRedirectUri);
    }

    @Override
    public ResponseEntity<SuccessResponse<Void>> kakaoLogin(String kakaoAccessToken, HttpServletRequest request, HttpServletResponse response) {

        return processLogin(kakaoAccessToken, "https://kapi.kakao.com/v2/user/me", LoginType.KAKAO, request, response);
    }

    @Override
    public String getGoogleAccessToken(String code) {

        return getAccessToken("https://oauth2.googleapis.com/token", code, googleClientId, googleClientSecret, googleRedirectUri);
    }

    @Override
    public ResponseEntity<SuccessResponse<Void>> googleLogin(String googleAccessToken, HttpServletRequest request, HttpServletResponse response) {

        return processLogin(googleAccessToken, "https://www.googleapis.com/oauth2/v2/userinfo", LoginType.GOOGLE, request, response);
    }

    @Override
    public String getNaverAccessToken(String code) {

        return getAccessToken("https://nid.naver.com/oauth2.0/token", code, naverClientId, naverClientSecret, naverRedirectUri);
    }

    @Override
    public ResponseEntity<SuccessResponse<Void>> naverLogin(String naverAccessToken, HttpServletRequest request, HttpServletResponse response) {

        return processLogin(naverAccessToken, "https://openapi.naver.com/v1/nid/me", LoginType.NAVER, request, response);
    }

    private String getAccessToken(String tokenUrl, String code, String clientId, String clientSecret, String redirectUri) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        JsonNode response = handleRestRequest(tokenUrl, body);
        if (response == null || !response.has("access_token")) {
            log.error("유효하지 않은 응답: {}", response);
            throw new CustomException(ErrorCode.INVALID_RESPONSE);
        }
        return response.get("access_token").asText();
    }

    private ResponseEntity<SuccessResponse<Void>> processLogin(String accessToken, String userInfoUrl, LoginType loginType, HttpServletRequest request, HttpServletResponse response) {

        log.info("processLogin() 메서드 호출");

        // WebClient 통해 사용자 정보 요청
        JsonNode userInfo = fetchUserInfo(accessToken, userInfoUrl);
        String email = extractUserInfo(userInfo, loginType, "email");
        String name = extractUserInfo(userInfo, loginType, "name");
        String nickname = extractUserInfo(userInfo, loginType, "nickname");

        // 유저 조회 또는 신규 등록
        if (name.isEmpty() || name.isBlank()) {
            getUserByEmailOrRegister(email, nickname, loginType);
        } else if (nickname.isBlank() || nickname.isEmpty()) {
            getUserByEmailOrRegister(email, name, loginType);
        }
        // JWT 토큰 생성
        String jwtAccessToken = jwtTokenUtils.generateAccessToken(email);
        String jwtRefreshToken = refreshTokenManager.saveRefreshToken(email);

        // 쿠키에 refreshToken, accessToken 추가
        refreshTokenManager.setRefreshTokenInCookie(jwtRefreshToken, request, response);
        accessTokenManager.setAccessTokenInCookie(jwtAccessToken, request, response);

        return buildResponseEntity();
    }

    private JsonNode fetchUserInfo(String accessToken, String userInfoUrl) {

        return webClient.get()
                .uri(userInfoUrl)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    private String extractUserInfo(JsonNode userInfo, LoginType loginType, String field) {

        return switch (loginType) {
            case KAKAO -> {
                if (field.equals("nickname")) {
                    // nickname은 두 가지 위치에서 가져올 수 있음
                    String nickname = userInfo.path("kakao_account").path("profile").path("nickname").asText();
                    if (nickname.isEmpty()) {
                        // `kakao_account.profile.nickname`이 비어있으면 `properties.nickname`으로 대체
                        nickname = userInfo.path("properties").path("nickname").asText();
                    }
                    yield nickname;
                } else {
                    yield userInfo.path("kakao_account").path(field).asText();
                }
            }
            case GOOGLE -> userInfo.path(field).asText();
            case NAVER -> userInfo.path("response").path(field).asText();
            default -> throw new CustomException(ErrorCode.INVALID_LOGIN_TYPE);
        };
    }

    private void getUserByEmailOrRegister(String email, String name, LoginType loginType) {

        User user;
        try {
            user = userReader.getUserByEmail(email);
        } catch (CustomException e) {
            log.info("신규 유저 저장: 추가 데이터 필요, email : {}", email);

            // 비밀번호는 null로 설정하고, isSocialLogin 플래그 추가
            user = new User(email, name, loginType, "미제공", null, true);

            userStore.store(user);
        }
    }

    private ResponseEntity<SuccessResponse<Void>> buildResponseEntity() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.successWithNoData("로그인 성공"));
    }

    private JsonNode handleRestRequest(String url, MultiValueMap<String, String> body) {

        log.info("handleRestRequest 메서드 호출");
        log.info("url : {}", url);

        try {
            return webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(body))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            log.error("REST 요청 실패: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.REST_REQUEST_FAILED);
        }
    }

}
