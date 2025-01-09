package com.wemo.backend.domain.auth;

import com.wemo.backend.domain.auth.token.service.JwtTokenUtils;
import com.wemo.backend.domain.auth.token.service.RefreshTokenManager;
import com.wemo.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAuthImpl implements UserAuth {

    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenManager refreshTokenManager;

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

        String refreshToken = refreshTokenManager.saveJwtRefreshToken(user.getEmail());
        log.info("유저 아이디 : {}, 생성 및 저장된 refreshToken : {}", user.getEmail(), refreshToken);
        return refreshToken;
    }

    /**
     * 응답 헤더에 accessToken, refreshToken 담아 반환
     *
     * @param user 유저 객체
     * @return accessToken, refreshToken 담긴 헤더
     */
    @Override
    public HttpHeaders generateHeaderTokens(User user) {

        HttpHeaders headers = new HttpHeaders();
        String accessToken = getAccessToken(user);
        String refreshToken = saveRefreshTokenToRedis(user);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.set("Refresh-Token", refreshToken);
        log.info("accessToken 및 refreshToken 생성완료 후 헤더에 추가 완료");
        return headers;
    }

}
