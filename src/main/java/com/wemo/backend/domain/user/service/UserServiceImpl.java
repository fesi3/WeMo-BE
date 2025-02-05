package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.attendance.entity.Attendance;
import com.wemo.backend.domain.attendance.service.AttendanceReader;
import com.wemo.backend.domain.attendance.service.AttendanceStore;
import com.wemo.backend.domain.auth.UserAuth;
import com.wemo.backend.domain.auth.token.entity.RefreshToken;
import com.wemo.backend.domain.auth.token.repository.RefreshTokenRepository;
import com.wemo.backend.domain.auth.token.service.AccessTokenManager;
import com.wemo.backend.domain.auth.token.service.RefreshTokenManager;
import com.wemo.backend.domain.like.entity.Likes;
import com.wemo.backend.domain.like.service.LikeReader;
import com.wemo.backend.domain.like.service.LikeStore;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meeting.service.MeetingReader;
import com.wemo.backend.domain.meetingMember.service.MeetingMemberStore;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.service.PlanReader;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.review.service.ReviewReader;
import com.wemo.backend.domain.user.dto.*;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStore userStore;
    private final UserReader userReader;
    private final UserAuth userAuth;
    private final RefreshTokenManager refreshTokenManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final AttendanceReader attendanceReader;
    private final LikeReader likeReader;
    private final LikeStore likeStore;
    private final ReviewReader reviewReader;
    private final AccessTokenManager accessTokenManager;
    private final MeetingReader meetingReader;
    private final PlanReader planReader;
    private final MeetingMemberStore meetingMemberStore;
    private final AttendanceStore attendanceStore;

    /**
     * 이메일 중복 검사
     *
     * @param email 이메일
     */
    @Override
    public void checkEmail(String email) {

        // 사용자 아이디 중복 검사
        userReader.checkEmailValid(email);
    }

    /**
     * 회원가입
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
     * 로그인
     *
     * @param signinRequest 아이디, 비밀번호로 로그인
     */
    @Override
    public void signin(SigninRequest signinRequest, HttpServletRequest request, HttpServletResponse response) {

        // 유저 검증 및 객체 조회
        User user = userReader.getUser(signinRequest.getEmail(), signinRequest.getPassword());
        log.info("사용자 {} 로그인 성공", user.getEmail());

        userAuth.generateHeaderTokens(user, request, response);
    }

    /**
     * 로그아웃
     *
     * @return 응답 메세지
     */
    @Override
    @Transactional
    public String signout(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = getTokenFromCookie(request, "Refresh-Token");

        // accessToken 검증 무효화 처리
        accessTokenManager.deleteAccessTokenInCookie(response);

        // refreshToken 삭제
        RefreshToken findRefreshToken = refreshTokenManager.getRefreshToken(refreshToken);
        refreshTokenManager.deleteRefreshTokenInCookie(response);
        refreshTokenRepository.delete(findRefreshToken);
        log.info("refreshToken 삭제 완료");

        return "로그아웃 성공";
    }

    /**
     * 회원 정보 조회
     *
     * @param email 사용자 이메일
     * @return 해당 유저의 회원 정보 (이메일, 닉네임, 회사명, 프로필이미지, 가입일자) 반환
     */
    @Override
    public UserInfoResponse getUserInfo(String email) {

        User user = userReader.getUserByEmail(email);

        List<Attendance> joinedPlanList = attendanceReader.getAttendanceByUser(user);
        List<Likes> likedPlanList = likeReader.getLikesByUser(user);
        List<Review> reviewList = reviewReader.getReviewByUser(user);

        log.info("사용자 {}의 회원 정보가 반환되었습니다.", user.getEmail());
        return UserInfoResponse.fromEntity(user, joinedPlanList.size(), likedPlanList.size(), reviewList.size());
    }

    /**
     * 내가 속한 모임 목록 조회
     *
     * @param email    사용자 이메일
     * @param pageable 페이징 처리 데이터
     * @return 유저가 속한 모임 목록
     */
    @Override
    public UserMeetingPagingResponse getMeetingList(String email, Pageable pageable) {

        return new UserMeetingPagingResponse(userRepository.getUserMeetingList(email, pageable));
    }

    /**
     * 내가 만든 모임 목록 조회
     *
     * @param email    사용자 이메일
     * @param pageable 페이징 처리 데이터
     * @return 유저가 만든 모임 목록
     */
    @Override
    public UserMeetingPagingResponse getMyMeetingList(String email, Pageable pageable) {

        return new UserMeetingPagingResponse(userRepository.getMyMeetingList(email, pageable));
    }

    /**
     * 내가 참여한 일정 목록 조회
     *
     * @param email    사용자 이메일
     * @param pageable 페이징 처리 데이터
     * @return 유저가 참여한 일정 목록
     */
    @Override
    @Transactional
    public UserPlanPagingResponse getPlanList(String email, Pageable pageable) {

        return new UserPlanPagingResponse(userRepository.getUserPlanList(email, pageable));
    }

    /**
     * 내가 만든 일정 목록 조회
     *
     * @param email    사용자 이메일
     * @param pageable 페이징 처리 데이터
     * @return 유저가 만든 일정 목록
     */
    @Override
    @Transactional
    public UserPlanPagingResponse getMyPlanList(String email, Pageable pageable) {

        return new UserPlanPagingResponse(userRepository.getMyPlanList(email, pageable));
    }

    /**
     * 내가 쓴 후기 목록 조회
     *
     * @param email    사용자 이메일
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
     * @param email    사용자 이메일
     * @param pageable 페이징 처리 데이터
     * @return 후기 작성 가능한 일정 목록
     */
    @Override
    @Transactional
    public UserPlanPagingResponse getPlanListReviewAvailable(String email, Pageable pageable) {

        return new UserPlanPagingResponse(userRepository.getUserPlanListReviewAvailable(email, pageable));
    }

    /**
     * 회원 정보 수정
     *
     * @param email   사용자 이메일
     * @param request 수정 요청 데이터 (닉네임, 프로필 이미지)
     * @return 수정된 회원 정보
     */
    @Override
    @Transactional
    public UserUpdateResponse updateProfile(String email, UserUpdateRequest request) {

        User user = userReader.getUserByEmail(email);
        User updateUser = user.update(request);

        return UserUpdateResponse.fromEntity(updateUser);
    }

    /**
     * 추가 데이터 저장
     *
     * @param email   사용자 이메일
     * @param request 추가 데이터 (회사명)
     */
    @Override
    @Transactional
    public void saveAdditionalUserData(String email, AdditionalDataRequest request) {

        User user = userReader.getUserByEmail(email);

        user.saveAdditionalData(request);
    }

    @Override
    @Transactional
    public UserPlanPagingResponse getPlanListV2(String email, Pageable pageable) {

        return new UserPlanPagingResponse(userRepository.getUserPlanListV2(email, pageable));
    }

    @Override
    @Transactional
    public UserPlanPagingResponse getMyPlanListV2(String email, Pageable pageable) {

        return new UserPlanPagingResponse(userRepository.getMyPlanListV2(email, pageable));
    }

    /**
     * 회원 탈퇴 기능
     *
     * @param email 사용자 이메일
     * @return 응답 메세지
     */
    @Override
    @Transactional
    public String withdraw(String email) {

        // 유저 객체 조회 및 softDelete 처리
        User user = userReader.getUserByEmail(email);
        user.softDelete();
        user.setDeletedUserNickname();

        // 유저가 생성한 데이터 softDelete 처리 (forEach 사용하여 간결화)
        meetingReader.getMeetingListByUser(user).forEach(Meeting::softDelete);
        planReader.getPlanListByUser(user).forEach(Plan::softDelete);
        reviewReader.getReviewByUser(user).forEach(Review::softDelete);

        // 모임 가입 내역, 일정 참여 내역, 좋아요 내역 batch delete 처리
        meetingMemberStore.deleteAllByUser(user);
        attendanceStore.deleteAllByUser(user);
        likeStore.deleteAllByUser(user);

        // TODO : 이미지 삭제 (관련 오류 발생 가능성 있음 → 기본 이미지 설정 고려)
        // imageStore.deleteAllByUser(user);

        return "정상적으로 탈퇴 처리되었습니다.";
    }

    private String getTokenFromCookie(HttpServletRequest request, String tokenName) {

        return Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> tokenName.equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst())
                .orElse(null);
    }

}
