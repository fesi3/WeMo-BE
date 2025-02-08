package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.repository.UserRepository;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.wemo.backend.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 이메일 중복 검사
     *
     * @param email 사용자 이메일
     */
    @Override
    public void checkEmailValid(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(EMAIL_ALREADY_IN_USE);
        }
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
            throw new CustomException(INVALID_PASSWORD);
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

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    /**
     * 이메일로 활동 중인 유저 조회
     *
     * @param email 사용자 이메일
     * @return 해당 유저 객체
     */
    @Override
    public User getActiveUserByEmail(String email) {

        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new CustomException(USER_WITHDRAW));
    }

    /**
     * 유저 객체 검증
     *
     * @param email    사용자 아이디
     * @param password 사용자 비밀번호
     * @return 해당 유저 객체 반환
     */
    @Override
    public User getUser(String email, String password) {

        User user = getUserByEmail(email);
        validatePassword(password, user.getPassword());
        return user;
    }

    /**
     * 비밀번호 확인
     *
     * @param rawPassword     입력된 비밀번호
     * @param encodedPassword 저장된 비밀번호
     */
    private void validatePassword(String rawPassword, String encodedPassword) {

        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomException(INVALID_PASSWORD);
        }
    }

}
