package com.wemo.backend.domain.user.service;

public interface UserReader {

    void checkEmailValid(String email);

    void checkPasswordValid(String password, String passwordCheck);

}
