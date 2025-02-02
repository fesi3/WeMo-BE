package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.user.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void checkEmail(String email);

    void signup(UserCreateRequest request);

    void signin(SigninRequest request, HttpServletResponse response);

    String signout(HttpServletRequest request, HttpServletResponse response);

    UserInfoResponse getUserInfo(String email);

    UserMeetingPagingResponse getMeetingList(String email, Pageable pageable);

    UserMeetingPagingResponse getMyMeetingList(String email, Pageable pageable);

    UserPlanPagingResponse getPlanList(String email, Pageable pageable);

    UserPlanPagingResponse getMyPlanList(String email, Pageable pageable);

    UserReviewPagingResponse getMyReviewList(String email, Pageable pageable);

    UserPlanPagingResponse getPlanListReviewAvailable(String email, Pageable pageable);

    UserUpdateResponse updateProfile(String email, UserUpdateRequest request);

    void saveAdditionalUserData(String email, AdditionalDataRequest request);

    UserPlanPagingResponse getPlanListV2(String email, Pageable pageable);

    UserPlanPagingResponse getMyPlanListV2(String email, Pageable pageable);

    String withdraw(String email);

}
