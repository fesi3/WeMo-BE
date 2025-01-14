package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.auth.JwtTokenProvider;
import com.wemo.backend.domain.auth.UserAuth;
import com.wemo.backend.domain.auth.token.entity.RefreshToken;
import com.wemo.backend.domain.auth.token.repository.RefreshTokenRepository;
import com.wemo.backend.domain.auth.token.service.RefreshTokenManager;
import com.wemo.backend.domain.auth.token.service.TokenBlacklistService;
import com.wemo.backend.domain.user.dto.*;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStore userStore;
    private final UserReader userReader;
    private final UserAuth userAuth;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenManager refreshTokenManager;
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

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
    public void signup(UserCreateRequest request) {

        // 사용자 아이디 중복 검사
        userReader.checkEmailValid(request.getEmail());
        // 비밀번호 확인 검사
        userReader.checkPasswordValid(request.getPassword(), request.getPasswordCheck());
        // 사용자 객체 생성 및 저장
        userStore.store(request.createUser());
    }

    /**
     * 2. 로그인
     *
     * @param signinRequest 아이디, 비밀번호로 로그인
     * @return 생성된 accessToken, refreshToken 담은 HttpHeaders
     */
    @Override
    public HttpHeaders signin(SigninRequest signinRequest) {

        // 유저 검증 및 객체 조회
        User user = userReader.getUser(signinRequest.getEmail(), signinRequest.getPassword());
        // 헤더에 accessToken 및 refreshToken 생성 후 전달
        return userAuth.generateHeaderTokens(user);
    }

    /**
     * 3. 로그아웃
     *
     * @param accessToken accessToken
     * @param refreshToken refreshToken
     * @return 응답 메세지
     */
    @Override
    @Transactional
    public String signout(String accessToken, String refreshToken) {

        // Bearer 제거
        String cleanToken = accessToken.replace("Bearer ", "");

        // accessToken 검증 및 블랙리스트 처리
        Long expiration = jwtTokenProvider.getExpiration(cleanToken);
        tokenBlacklistService.addToBlacklist(cleanToken, expiration);
        log.info("accessToken 블랙리스트에 추가 완료");

        // refreshToken 삭제
        RefreshToken findRefreshToken = refreshTokenManager.getRefreshToken(refreshToken);
        refreshTokenRepository.delete(findRefreshToken);
        log.info("refreshToken 삭제 완료");

        return "로그아웃 성공";
    }

    /**
     * 4. 회원 정보 조회
     *
     * @param email 이메일
     * @return 해당 유저의 회원 정보 (이메일, 닉네임, 회사명, 프로필이미지, 가입일자) 반환
     */
    @Override
    public UserInfoResponse getUserInfo(String email) {

        User user = userReader.getUserByEmail(email);
        return UserInfoResponse.fromEntity(user);
    }

    /**
     * 내가 속한 모임 목록 조회
     *
     * @param email 이메일
     * @param pageable 페이징 처리 데이터
     * @return 유저가 속한 모임 목록
     */
    @Override
    public UserMeetingPagingResponse getMyMeetingList(String email, Pageable pageable) {

        return new UserMeetingPagingResponse(userRepository.getUserMeetingList(email, pageable));
    }

    /**
     * 내가 참여한 일정 목록 조회
     *
     * @param email 이메일
     * @param pageable 페이징 처리 데이터
     * @return 유저가 참여한 일정 목록
     */
    @Override
    public UserPlanPagingResponse getMyPlanList(String email, Pageable pageable) {

        return new UserPlanPagingResponse(userRepository.getUserPlanList(email, pageable));
    }

    /**
     * 내가 쓴 후기 목록 조회
     *
     * @param email 이메일
     * @param pageable 페이징 처리 데이터
     * @return 유저가 작성한 후기 목록
     */
    @Override
    public UserReviewPagingResponse getMyReviewList(String email, Pageable pageable) {

        return new UserReviewPagingResponse(userRepository.getUserReviewList(email, pageable));
    }

    /**
     * 후기 작성 가능한 일정 목록 조회
     *
     * @param email 이메일
     * @param pageable 페이징 처리 데이터
     * @return 후기 작성 가능한 일정 목록
     */
    @Override
    public UserPlanPagingResponse getPlanListReviewAvailable(String email, Pageable pageable) {

        return new UserPlanPagingResponse(userRepository.getUserPlanListReviewAvailable(email, pageable));
    }

}
