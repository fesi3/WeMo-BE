package com.wemo.backend.domain.user.repository.querydsl;

import com.wemo.backend.domain.user.dto.UserMeetingListResponse;
import com.wemo.backend.domain.user.dto.UserPlanListResponse;
import com.wemo.backend.domain.user.dto.UserPlanReviewableListResponse;
import com.wemo.backend.domain.user.dto.UserReviewListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryDsl {

    Page<UserMeetingListResponse> getUserMeetingList(String email, Pageable pageable);

    Page<UserPlanListResponse> getUserPlanList(String email, Pageable pageable);

    Page<UserReviewListResponse> getUserReviewList(String email, Pageable pageable);

    Page<UserPlanReviewableListResponse> getUserPlanListReviewAvailable(String email, Pageable pageable);

}
