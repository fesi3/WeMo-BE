package com.wemo.backend.domain.auth;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemo.backend.domain.auth.token.service.TokenBlacklistService;
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
    private final TokenBlacklistService tokenBlacklistService;

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
        if (tokenBlacklistService.isBlacklisted(accessToken)) {
            sendErrorResponse(response, "이미 로그아웃된 토큰입니다. 로그인 후 다시 시도해주세요.", HttpStatus.UNAUTHORIZED);
            return false;
        }
        try {
            jwtTokenProvider.parseToken(accessToken);
            return true;
        } catch (TokenExpiredException e) {
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

        // accessToken 과 refreshToken 모두 유효하지 않은 경우 로그아웃 실패
        if (!jwtTokenProvider.validateAccessToken(accessToken) && !jwtTokenProvider.validateRefreshToken(refreshToken)) {
            sendErrorResponse(response, "accessToken과 refreshToken 모두 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
            return false;
        }

        // accessToken 만료 여부에 관계없이 refreshToken 이 만료된 경우 로그아웃 실패
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            sendErrorResponse(response, "유효하지 않은 refreshToken입니다.", HttpStatus.BAD_REQUEST);
            return false;
        }

        // accessToken 이 유효하지 않은 경우 (값이 존재하지 않거나 이미 로그아웃된 토큰인 경우)
        if (!jwtTokenProvider.validateAccessToken(accessToken)) {
            sendErrorResponse(response, "유효하지 않은 accessToken입니다.", HttpStatus.BAD_REQUEST);
            return false;
        }
        return true;
    }

    private boolean shouldSkipFilter(String requestURI, String method, String accessToken) {

        return EXCLUDED_PATHS.stream().anyMatch(requestURI::startsWith)
                || (requestURI.startsWith("/api/meetings") && "GET".equalsIgnoreCase(method) && accessToken == null)
                || (requestURI.startsWith("/api/plans") && "GET".equalsIgnoreCase(method) && !requestURI.contains("like") && accessToken == null)
                || (requestURI.startsWith("/api/reviews") && "GET".equalsIgnoreCase(method) && accessToken == null);
    }

    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus httpStatus) throws IOException {

        if (response.isCommitted()) return; // 이미 응답이 전송된 경우 중단

        response.setStatus(httpStatus.value());
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(false, message, httpStatus);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }

    private void logRequestInfo(HttpServletRequest request) {

        log.info("User-Agent: {}", request.getHeader("User-Agent"));
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
