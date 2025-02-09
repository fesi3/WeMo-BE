package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.auth.token.service.RefreshTokenManager;
import com.wemo.backend.domain.user.dto.SigninRequest;
import com.wemo.backend.domain.user.dto.UserCreateRequest;
import com.wemo.backend.domain.user.entity.LoginType;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.repository.UserRepository;
import com.wemo.backend.global.exception.CustomException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static com.wemo.backend.global.exception.ErrorCode.*;
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

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

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
    void tearDown() {

        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("Success")
    class Success {

        @Test
        @DisplayName("회원 가입에 성공합니다.")
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
        @DisplayName("로그인에 성공합니다.")
        void signin_Success() {
            // given
            String email = "existing@example.com";
            String password = "password123";
            SigninRequest signinRequest = new SigninRequest(email, password);

            // when
            userService.signin(signinRequest, request, response);

            // then
            assertTrue(refreshTokenManager.existsByEmail(email)); // ✅ refreshToken 저장 여부 확인
        }

//        @Test
//        @DisplayName("로그아웃에 성공합니다.")
//        void logout_Success() {
//            // given
//            String email = "existing@example.com";
//            SigninRequest signinRequest = new SigninRequest(email, "password123");
//
//            // 로그인하여 토큰 발급
//            userService.signin(signinRequest, request, response);
//
//            // 로그인 후 발급된 refreshToken을 쿠키로 가져오기
//            Cookie accessTokenCookie = response.getCookie("accessToken");
//            Cookie refreshTokenCookie = response.getCookie("Refresh-Token");
//
//            // when
//            // `request` 객체에 `Refresh-Token` 쿠키를 직접 추가
//            request.setCookies(new Cookie("accessToken", accessTokenCookie.getValue())); // 쿠키 주입
//            request.setCookies(new Cookie("Refresh-Token", refreshTokenCookie.getValue())); // 쿠키 주입
//
//            // 로그아웃 실행
//            userService.signout(request, response);
//
//            // then
//            // 쿠키가 만료되었는지 확인
//            Cookie accessTokenCookieAfterLogout = response.getCookie("accessToken");
//            Cookie refreshTokenCookieAfterLogout = response.getCookie("Refresh-Token");
//
//            assertNotNull(accessTokenCookieAfterLogout);
//            assertNotNull(refreshTokenCookieAfterLogout);
//
//            // 쿠키 만료 처리
//            accessTokenCookieAfterLogout.setMaxAge(0);  // 만료 설정
//            refreshTokenCookieAfterLogout.setMaxAge(0);  // 만료 설정
//
//            assertEquals(0, accessTokenCookieAfterLogout.getMaxAge()); // accessToken 만료 확인
//            assertEquals(0, refreshTokenCookieAfterLogout.getMaxAge()); // Refresh-Token 만료 확인
//
//            // Redis에서 refreshToken이 삭제되었는지 확인
//            assertFalse(refreshTokenManager.existsByEmail(email));
//        }
//
//        @Test
//        @DisplayName("로그아웃에 성공하면 RefreshToken이 삭제되고 AccessToken 쿠키가 만료됨")
//        void logout_Success_cookie() {
//            // given (로그인 상태에서 로그아웃 요청)
//            String email = "existing@example.com";
//            SigninRequest signinRequest = new SigninRequest(email, "password123");
//
//            MockHttpServletRequest mockRequest = new MockHttpServletRequest();
//            MockHttpServletResponse mockResponse = new MockHttpServletResponse();
//
//            userService.signin(signinRequest, mockRequest, mockResponse); // 로그인 진행
//
//            // 로그아웃 전에 email이 정상적으로 있는지 확인
//            assertNotNull(email, "로그아웃 시 email 값이 null이면 안됩니다!");
//
//            // when (로그아웃 실행)
//            userService.signout(mockRequest, mockResponse);
//
//            // then (Redis에서 RefreshToken이 삭제되었는지 확인)
//            assertFalse(refreshTokenManager.existsByEmail(email));
//
//            // 그리고 response에서 AccessToken 쿠키가 만료되었는지 확인
//            Cookie[] cookies = mockResponse.getCookies();
//            Cookie accessTokenCookie = Arrays.stream(cookies)
//                    .filter(cookie -> "accessToken".equals(cookie.getName()))
//                    .findFirst()
//                    .orElse(null);
//
//            assertNotNull(accessTokenCookie);
//            assertEquals(0, accessTokenCookie.getMaxAge()); // ✅ 만료된 쿠키인지 확인
//        }

    }

    @Nested
    @DisplayName("Failure")
    class Failure {
        @Test
        @DisplayName("회원 가입 시 기존에 존재하는 이메일로 가입하는 경우 실패합니다.")
        void signup_Failure_duplicate_email() {
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

        @Test
        @DisplayName("가입되지 않은 아이디로 로그인 시 실패합니다.")
        void signin_Failure_user_not_exist() {
            // given
            SigninRequest signinRequest = new SigninRequest(
                    "newUser@example.com",
                    "password"
            );

            // when & then
            CustomException exception = assertThrows(CustomException.class, () -> userService.signin(signinRequest, request, response));
            assertEquals(USER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("등록된 비밀번호와 일치하지 않는 경우 로그인에 실패합니다.")
        void signin_Failure_validPassword() {
            // given
            SigninRequest signinRequest = new SigninRequest(
                    "existing@example.com",
                    "anotherPassword"
            );

            // when & then
            CustomException exception = assertThrows(CustomException.class, () -> userService.signin(signinRequest, request, response));
            assertEquals(INVALID_PASSWORD, exception.getErrorCode());
        }

    }


}
