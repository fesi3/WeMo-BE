package com.wemo.backend.domain.auth;

import com.wemo.backend.domain.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;

public interface UserAuth {

    String getAccessToken(User user);

    String saveRefreshTokenToRedis(User user);

    HttpHeaders generateHeaderTokens(User user, HttpServletResponse response);

}
