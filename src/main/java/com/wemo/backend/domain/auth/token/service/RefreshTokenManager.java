package com.wemo.backend.domain.auth.token.service;

import com.wemo.backend.domain.auth.token.entity.RefreshToken;
import com.wemo.backend.domain.auth.token.repository.RefreshTokenRepository;
import com.wemo.backend.global.exception.CustomException;
import com.wemo.backend.global.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.wemo.backend.global.exception.ErrorCode.ILLEGAL_REFRESH_TOKEN_NOT_VALID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenManager {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final AccessTokenManager accessTokenManager;

    /**
     * JWT refreshToken 생성 및 저장
     *
     * @param email 유저 아이디
     * @return 저장된 refreshToken
     */
    @Transactional
    public String saveRefreshToken(final String email) {

        String refreshToken = jwtTokenUtils.generateRefreshToken(email);

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByEmail(email);

        if (existingToken.isPresent()) {
            refreshTokenRepository.delete(existingToken.get());
            log.info("기존에 존재하는 refreshToken 삭제 완료");
        }

        refreshTokenRepository.save(new RefreshToken(refreshToken, email));

        return refreshToken;
    }

    /**
     * refreshToken 유효성 검증
     *
     * @param refreshToken jwt 형태의 refreshToken
     * @return 검증이 완료된 refreshToken 객체 반환
     */
    public RefreshToken getRefreshToken(String refreshToken) {

        return refreshTokenRepository.findByEmail(refreshToken).orElseThrow(
                () -> new CustomException(ILLEGAL_REFRESH_TOKEN_NOT_VALID)
        );

    }

    /**
     * refreshToken 유효성 검증
     *
     * @param refreshToken 검증할 refreshToken
     * @return 유저 아이디 반환, 유효하지 않은 경우 null
     */
    public String validateRefreshToken(String refreshToken) {

        Optional<RefreshToken> token = refreshTokenRepository.findByEmail(refreshToken);
        return token.map(RefreshToken::email).orElse(null);
    }

    /**
     * refreshToken 검증하고 새로운 토큰 생성 및 발급
     */
    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {

        log.info("accessToken 재발급 요청 메서드 호출");

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null)
            for (Cookie cookie : cookies) {

                if (cookie.getName().equals("Refresh-Token")) {
                    refreshToken = cookie.getValue();
                    log.info("토큰 재발급 요청의 refreshToken : {}", refreshToken);
                }
            }

        if (refreshToken == null || refreshToken.isEmpty()) {

            throw new CustomException(ErrorCode.MISSING_AUTHORIZATION_REFRESH_TOKEN);
        }

        String email = validateRefreshToken(refreshToken);
        if (email == null) {
            throw new CustomException(ILLEGAL_REFRESH_TOKEN_NOT_VALID);
        }

        String newAccessToken = jwtTokenUtils.generateAccessToken(email);
        accessTokenManager.setAccessTokenInCookie(newAccessToken, response);

        log.info("토큰 생성 후 헤더에 담기 완료!");
    }

    /**
     * refreshToken 을 쿠키에 저장
     *
     * @param refreshToken refreshToken 값
     */
    public void setRefreshTokenInCookie(String refreshToken, HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("Refresh-Token", refreshToken)
                .maxAge(24 * 60 * 60)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void deleteRefreshTokenInCookie(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("Refresh-Token", null)
                .maxAge(0) // 쿠키 즉시 만료
                .sameSite("None") // 쿠키 SameSite 설정
                .secure(true) // HTTPS 환경에서만 전송
                .httpOnly(true) // JavaScript에서 접근 불가
                .path("/") // 모든 경로에서 유효
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString()); // 헤더에 쿠키 추가

        log.info("Refresh-Token 쿠키 삭제 완료");

    }

}
