package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.wemo.backend.domain.user.entity.LoginType.GENERAL;

@Component
@RequiredArgsConstructor
public class UserStoreImpl implements UserStore {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void store(User initUser) {

        User user = User.builder()
                .email(initUser.getEmail())
                .nickname(initUser.getNickname())
                .companyName(initUser.getCompanyName())
                .password(passwordEncoder.encode(initUser.getPassword()))
                .profileImagePath("https://ryungbucket.s3.ap-northeast-2.amazonaws.com/2480fdf5-c3ff-4348-82bf-b4daa4084ead.jpg")
                .loginType(GENERAL)
                .build();

        userRepository.save(user);

    }

}
