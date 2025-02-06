package com.wemo.backend.domain.auth;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemo.backend.domain.auth.token.service.AccessTokenManager;
import com.wemo.backend.global.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final AccessTokenManager accessTokenManager;

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/auths/check-email", "/api/auths/signin", "/api/auths/signup", "/swagger-ui/", "/swagger-ui.html",
            "/v3/api-docs", "/api/regions", "/api/auths/reissue", "/login/oauth2/callback/kakao",
            "/login/oauth2/callback/google", "/login/oauth2/callback/naver"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {

        try {
            logRequestInfo(request);

            String accessToken = getTokenFromCookie(request, "accessToken");
            String refreshToken = getTokenFromCookie(request, "Refresh-Token");
            log.info("accessToken: {}, refreshToken : {}", accessToken, refreshToken);

            if (shouldSkipFilter(request.getRequestURI(), request.getMethod(), accessToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            if ("/api/auths/signout".equals(request.getRequestURI()) && !validateLogoutRequest(accessToken, refreshToken, response)) {
                return; // 응답 후 중단
            }

            if (!validateAccessToken(accessToken, response)) {
                return; // 응답 후 중단
            }

            setAuthentication(accessToken, request.getRequestURI());
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("필터 처리 중 에러 발생: {}", e.getMessage());
            if (!response.isCommitted()) { // 응답이 이미 전송되지 않은 경우에만 에러 응답 전송
                sendErrorResponse(response, "서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private boolean validateAccessToken(String accessToken, HttpServletResponse response) throws IOException {

        if (accessToken == null || accessToken.isEmpty()) {
            sendErrorResponse(response, "accessToken이 요청에 포함되지 않았습니다.", HttpStatus.BAD_REQUEST);
            return false;
        }
        try {
            jwtTokenProvider.parseToken(accessToken);
            return true;
        } catch (TokenExpiredException e) {
            // 만료된 토큰인 경우 쿠키에서 무력화
            accessTokenManager.deleteAccessTokenInCookie(response);
            sendErrorResponse(response, "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED);
            return false;
        } catch (Exception e) {
            sendErrorResponse(response, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    private void setAuthentication(String accessToken, String requestURI) {

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
    }

    private boolean validateLogoutRequest(String accessToken, String refreshToken, HttpServletResponse response)
            throws IOException {

        boolean isAccessTokenValid = jwtTokenProvider.validateAccessToken(accessToken);
        boolean isRefreshTokenValid = refreshToken != null && jwtTokenProvider.validateRefreshToken(refreshToken);

        // 1. accessToken 이 유효하지 않으면 로그아웃 실패
        if (!isAccessTokenValid) {
            // refreshToken 도 유효하지 않으면 재로그인 필요
            if (!isRefreshTokenValid) {
                sendErrorResponse(response, "재로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
                return false;
            }
            log.info("유효하지 않은 accessToken이지만, 유효한 refreshToken이 있어 로그아웃을 진행합니다.");
        }

        // 2. refreshToken 이 유효하지 않아도 로그아웃 진행 (로그만 남김)
        if (!isRefreshTokenValid) {
            log.info("유효하지 않은 refreshToken입니다. 만료된 refreshToken으로 로그아웃 처리됩니다.");
        }

        return true; // 로그아웃 요청이 유효함
    }

    private boolean shouldSkipFilter(String requestURI, String method, String accessToken) {

        return EXCLUDED_PATHS.stream().anyMatch(requestURI::startsWith)
                || (requestURI.startsWith("/api/meetings") && "GET".equalsIgnoreCase(method) && accessToken == null)
                || (requestURI.startsWith("/api/plans") && "GET".equalsIgnoreCase(method) && !requestURI.contains("like") && accessToken == null)
                || (requestURI.startsWith("/api/reviews") && "GET".equalsIgnoreCase(method));
    }

    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus httpStatus) throws IOException {

        if (response.isCommitted()) return; // 이미 응답이 전송된 경우 중단

        response.setStatus(httpStatus.value());
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(false, message, httpStatus);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }

    private void logRequestInfo(HttpServletRequest request) {

        log.info("요청 IP: {}, 요청 URI: {}", request.getRemoteAddr(), request.getRequestURI());
    }

    private String getTokenFromCookie(HttpServletRequest request, String tokenName) {

        return Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> tokenName.equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst())
                .orElse(null);
    }

}
