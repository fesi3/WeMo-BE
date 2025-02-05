package com.wemo.backend.domain.auth;

import com.wemo.backend.domain.auth.token.service.AccessTokenManager;
import com.wemo.backend.domain.auth.token.service.JwtTokenUtils;
import com.wemo.backend.domain.auth.token.service.RefreshTokenManager;
import com.wemo.backend.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAuthImpl implements UserAuth {

    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenManager refreshTokenManager;
    private final AccessTokenManager accessTokenManager;

    /**
     * Jwt accessToken 생성
     *
     * @param user 유저 객체
     * @return 생성된 accessToken
     */
    @Override
    public String getAccessToken(User user) {

        String accessToken = jwtTokenUtils.generateAccessToken(user.getEmail());
        log.info("유저 아이디 : {}, 생성된 accessToken : {}", user.getEmail(), accessToken);
        return accessToken;
    }

    /**
     * refreshToken 생성 및 Redis 저장
     *
     * @param user 유저 객체
     * @return 생성된 refreshToken
     */
    @Override
    public String saveRefreshTokenToRedis(User user) {

        String refreshToken = refreshTokenManager.saveRefreshToken(user.getEmail());
        log.info("유저 아이디 : {}, 생성 및 저장된 refreshToken : {}", user.getEmail(), refreshToken);
        return refreshToken;
    }

    /**
     * 응답 쿠키에 accessToken, refreshToken 담아 반환
     *
     * @param user 유저 객체
     */
    @Override
    public void generateHeaderTokens(User user, HttpServletRequest request, HttpServletResponse response) {

        String accessToken = getAccessToken(user);
        String refreshToken = saveRefreshTokenToRedis(user);

        accessTokenManager.setAccessTokenInCookie(accessToken, request, response);
        refreshTokenManager.setRefreshTokenInCookie(refreshToken, response);
        log.info("accessToken 및 refreshToken 생성 완료");
        log.info("refreshToken 쿠키로 전달 완료");

    }

}
