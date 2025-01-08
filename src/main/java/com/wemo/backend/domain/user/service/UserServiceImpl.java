package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.user.dto.UserCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStore userStore;
    private final UserReader userReader;

    /**
     * 0. 이메일 중복 검사
     *
     * @param email 이메일
     */
    @Override
    public void checkEmail(String email) {
        // 사용자 아이디 중복 검사
        userReader.checkEmailValid(email);
    }

    /**
     * 1. 회원가입
     *
     * @param request 이메일, 닉네임, 회사명, 비밀번호로 회원가입
     */
    @Override
    @Transactional
    public void createUser(UserCreateRequest request) {

        // 사용자 아이디 중복 검사
        userReader.checkEmailValid(request.getEmail());
        // 비밀번호 확인 검사
        userReader.checkPasswordValid(request.getPassword(), request.getPasswordCheck());
        // 사용자 객체 생성 및 저장
        userStore.store(request.createUser());
    }

}
