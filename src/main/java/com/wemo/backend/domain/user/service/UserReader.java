package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.user.entity.User;

public interface UserReader {

    void checkEmailValid(String email);

    void checkPasswordValid(String password, String passwordCheck);

    User getUser(String email, String password);

}
