package com.wemo.backend.domain.auth;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemo.backend.domain.auth.token.service.TokenBlacklistService;
import com.wemo.backend.global.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        try {
            String requestURI = request.getRequestURI();
            String method = request.getMethod(); // HTTP 메서드 가져오기

            String accessToken = jwtTokenProvider.resolveToken(request);
            String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

            log.info("요청 URI: {}, accessToken: {}", requestURI, accessToken);
            log.info("refreshToken: {}", refreshToken);

            // 예외 경로 설정 (로그인, 회원가입 등)
            if (isExcludedPath(requestURI)) {
                filterChain.doFilter(request, response); // 필터를 건너뜀
                return;
            }

            if (requestURI.startsWith("/api/plans") && "GET".equalsIgnoreCase(method)) {
                // 토큰이 없는 경우 비회원으로 처리
                if (accessToken == null) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            if (requestURI.startsWith("/api/reviews") && "GET".equalsIgnoreCase(method)) {
                // 토큰이 없는 경우 비회원으로 처리
                if (accessToken == null) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            // 로그아웃 요청 처리
            if ("/api/auths/signout".equals(requestURI)) {
                if (!validateLogoutRequest(accessToken, refreshToken, response)) {
                    // 유효성 검증 실패 시 요청 중단
                    return;
                }
                // 유효성 검증에 성공하면 컨트롤러로 요청 전달
            }

            // 일반 요청 처리
            if (accessToken == null || accessToken.isEmpty()) {
                log.warn("accessToken이 존재하지 않습니다.");
                sendErrorResponse(response, "accessToken이 요청에 포함되지 않았습니다.", HttpStatus.BAD_REQUEST);
                return;
            }

            else {
                // 1. accessToken이 블랙리스트에 포함되어 있는지 확인
                if (tokenBlacklistService.isBlacklisted(accessToken)) {
                    sendErrorResponse(response, "이미 로그아웃된 토큰입니다. 로그인 후 다시 시도해주세요.", HttpStatus.UNAUTHORIZED);
                    return;
                }
                try {
                    // 토큰 유효성 검증
                    jwtTokenProvider.parseToken(accessToken);
                } catch (TokenExpiredException e) {
                    log.error("만료된 토큰입니다. {}", e.getMessage());
                    sendErrorResponse(response, "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED);
                    return; // 필터 체인을 종료
                } catch (Exception e) {
                    log.error("토큰 검증 중 에러 발생: {}", e.getMessage());
                    sendErrorResponse(response, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
                    return;
                }

                // 2. accessToken 유효성 검증 및 인증 정보 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);

            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("필터 처리 중 에러 발생: {}", e.getMessage());
            sendErrorResponse(response, "서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validateLogoutRequest(String accessToken, String refreshToken, HttpServletResponse response)
            throws IOException {
        boolean isAccessTokenValid = jwtTokenProvider.validateAccessToken(accessToken);
        boolean isRefreshTokenValid = jwtTokenProvider.validateRefreshToken(refreshToken);

        // accessToken과 refreshToken 모두 유효하지 않은 경우
        if (!isAccessTokenValid && !isRefreshTokenValid) {
            sendErrorResponse(response, "accessToken과 refreshToken 모두 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
            return false;
        }

        // refreshToken이 유효하지 않은 경우
        if (!isRefreshTokenValid) {
            sendErrorResponse(response, "유효하지 않은 refreshToken입니다.", HttpStatus.BAD_REQUEST);
            return false;
        }

        // accessToken이 유효하지 않은 경우 (값이 존재하지 않거나 이미 로그아웃된 토큰인 경우)
        if (!isAccessTokenValid) {
            sendErrorResponse(response, "유효하지 않은 accessToken입니다.", HttpStatus.BAD_REQUEST);
            return false;
        }

        // 둘 다 유효한 경우
        return true;
    }

    // 공통적인 에러 응답을 보내는 메서드
    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus httpStatus) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(false, message, httpStatus);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }

    private boolean isExcludedPath(String requestURI) {

        return requestURI.startsWith("/api/auths/check-email") ||
                requestURI.startsWith("/api/auths/signin") ||
                requestURI.startsWith("/api/auths/signup") ||
                requestURI.startsWith("/swagger-ui/") ||
                requestURI.startsWith("/swagger-ui.html") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/api/meetings");
    }

}
