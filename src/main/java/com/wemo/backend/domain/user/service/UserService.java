package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.user.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void checkEmail(String email);

    void signup(UserCreateRequest request);

    HttpHeaders signin(SigninRequest request);

     String signout(String accessToken, String refreshToken);

    UserInfoResponse getUserInfo(String email);

    UserMeetingPagingResponse getMyMeetingList(String email, Pageable pageable);

    UserPlanPagingResponse getMyPlanList(String email, Pageable pageable);

    UserReviewPagingResponse getMyReviewList(String email, Pageable pageable);

    UserPlanPagingResponse getPlanListReviewAvailable(String email, Pageable pageable);

}
