package com.wemo.backend.domain.auth;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // header에서 token을 받아온다
        String token = jwtTokenProvider.resolveToken(request);
        log.info("token : {}", token);

        String requestURI = request.getRequestURI();

        // 토큰을 검증하고 SecurityContext에 인증 정보 저장
        Authentication authentication = jwtTokenProvider.getAuthentication(token, response);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);

        log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        filterChain.doFilter(request, response);
    }

}
