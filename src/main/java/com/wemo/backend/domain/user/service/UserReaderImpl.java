package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.user.repository.UserRepository;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.wemo.backend.global.exception.ErrorCode.ILLEGAL_EMAIL_DUPLICATION;
import static com.wemo.backend.global.exception.ErrorCode.ILLEGAL_PASSWORD_VALID;

@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

    private final UserRepository userRepository;

    @Override
    public void checkEmailValid(String email) {
        userRepository.findByEmail(email).ifPresent(
                user -> {
                    throw new CustomException(ILLEGAL_EMAIL_DUPLICATION);
                }
        );
    }

    @Override
    public void checkPasswordValid(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) throw new CustomException(ILLEGAL_PASSWORD_VALID);
    }

}
