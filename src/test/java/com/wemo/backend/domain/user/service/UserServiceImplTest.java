package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.auth.token.service.RefreshTokenManager;
import com.wemo.backend.domain.user.dto.SigninRequest;
import com.wemo.backend.domain.user.dto.UserCreateRequest;
import com.wemo.backend.domain.user.entity.LoginType;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.repository.UserRepository;
import com.wemo.backend.global.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static com.wemo.backend.global.exception.ErrorCode.EMAIL_ALREADY_IN_USE;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenManager refreshTokenManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletResponse response;

    @BeforeEach
    @Transactional
    void setUp() {

        userRepository.deleteAll();

        User user = User.builder()
                .email("existing@example.com")
                .nickname("nickname")
                .profileImagePath("https://profileImage.jpg")
                .loginType(LoginType.GENERAL)
                .companyName("company")
                .password(passwordEncoder.encode("password123"))
                .build();

        userRepository.save(user);
    }

    @AfterEach
    @Transactional
    void tearDown() {

        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("Success")
    class Success {

        @Test
        @DisplayName("회원 가입 해피 케이스 테스트")
        @Transactional
        void signup_Success() {
            // given
            UserCreateRequest request = new UserCreateRequest(
                    "newuser@example.com",
                    "nickname",
                    "company",
                    "password123",
                    "password123"
            );

            // when
            userService.signup(request);

            // then
            assertTrue(userRepository.findByEmail("newuser@example.com").isPresent());
        }
        
        @Test
        @DisplayName("로그인 해피 케이스 테스트")
        void signin_Success() {
            // given
            String email = "existing@example.com";
            String password = "password123";

            SigninRequest signinRequest = new SigninRequest(email, password);

            // when
            userService.signin(signinRequest, response);

            // then
            assertTrue(refreshTokenManager.existsByEmail(email)); // ✅ refreshToken 저장 여부 확인
        }

    }

    @Nested
    @DisplayName("Failure")
    class Failure {
        @Test
        @DisplayName("회원 가입 이메일 중복 예외 케이스 테스트")
        @Transactional
        void signup_Failure() {
            // given
            UserCreateRequest request = new UserCreateRequest(
                    "existing@example.com",
                    "nickname",
                    "company",
                    "password123",
                    "password123"
            );

            // when & then
            CustomException exception = assertThrows(CustomException.class, () -> userService.signup(request));
            assertEquals(EMAIL_ALREADY_IN_USE, exception.getErrorCode());

        }

    }

}
