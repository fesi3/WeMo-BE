package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.repository.UserRepository;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.wemo.backend.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

    private final UserRepository userRepository;

    /**
     * 이메일 중복 검사
     *
     * @param email 사용자 이메일
     */
    @Override
    public void checkEmailValid(String email) {

        findUserByEmail(email).ifPresent(user -> {
            throw new CustomException(ILLEGAL_EMAIL_DUPLICATION);
        });
    }

    /**
     * 비밀번호 일치 검사
     *
     * @param password      새 비밀번호
     * @param passwordCheck 확인용 비밀번호
     */
    @Override
    public void checkPasswordValid(String password, String passwordCheck) {

        if (!password.equals(passwordCheck)) {
            throw new CustomException(ILLEGAL_PASSWORD_NOT_VALID);
        }
    }

    /**
     * 이메일로 유저 조회
     *
     * @param email 사용자 이메일
     * @return 해당 유저 객체
     */
    @Override
    public User getUserByEmail(String email) {

        return findUserByEmail(email)
                .orElseThrow(() -> new CustomException(ILLEGAL_USER_NOT_EXIST));
    }

    /**
     * 이메일로 유저 조회
     *
     * @param email 사용자 이메일
     * @return Optional<User>
     */
    private Optional<User> findUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

}
