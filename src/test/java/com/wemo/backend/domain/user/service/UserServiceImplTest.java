package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.user.dto.UserCreateRequest;
import com.wemo.backend.domain.user.entity.LoginType;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.repository.UserRepository;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.wemo.backend.global.exception.ErrorCode.ILLEGAL_EMAIL_DUPLICATION;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserReader userReader;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        User user = User.builder()
                .email("existing@example.com")
                .nickname("nickname")
                .profileImagePath("https://profileImage.jpg")
                .loginType(LoginType.GENERAL)
                .companyName("company")
                .password("password123")
                .build();

        userRepository.save(user);
    }

    @Nested
    @DisplayName("Success")
    class Success {

        @Test
        @DisplayName("회원 가입 해피 케이스 테스트")
        @Transactional
        void signup_Success() {
            // Given
            UserCreateRequest request = new UserCreateRequest(
                    "newuser@example.com",
                    "nickname",
                    "company",
                    "password123",
                    "password123"
            );

            // When
            userService.signup(request);

            // Then
            assertTrue(userRepository.findByEmail("newuser@example.com").isPresent());
        }

    }

    @Nested
    @DisplayName("Failure")
    class Failure {
        @Test
        @DisplayName("회원 가입 이메일 중복 예외 케이스 테스트")
        @Transactional
        void signup_Success() {
            // Given
            UserCreateRequest request = new UserCreateRequest(
                    "existing@example.com",
                    "nickname",
                    "company",
                    "password123",
                    "password123"
            );

            // When & Then
            CustomException exception = assertThrows(CustomException.class, () -> userService.signup(request));
            assertEquals(ILLEGAL_EMAIL_DUPLICATION, exception.getErrorCode());

        }

    }

}
