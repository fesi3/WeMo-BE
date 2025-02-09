package com.wemo.backend.domain.auth;

import com.wemo.backend.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserAuth {

    String getAccessToken(User user);

    String saveRefreshTokenToRedis(User user);

    void generateCookieTokens(User user, HttpServletRequest request, HttpServletResponse response);

}
