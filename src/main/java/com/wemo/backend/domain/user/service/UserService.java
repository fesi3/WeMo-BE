package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.user.dto.SigninRequest;
import com.wemo.backend.domain.user.dto.UserCreateRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void checkEmail(String email);

    void signup(UserCreateRequest request);

    HttpHeaders signin(SigninRequest request);

}
