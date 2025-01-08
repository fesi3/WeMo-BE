package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.user.dto.EmailCheckRequest;
import com.wemo.backend.domain.user.dto.UserCreateRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void createUser(UserCreateRequest request);

    void checkEmail(String email);

}
