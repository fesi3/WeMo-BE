package com.wemo.backend.domain.user.repository.querydsl;

import com.wemo.backend.domain.user.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryDsl {

    Page<UserMeetingListResponse> getUserMeetingList(String email, Pageable pageable);

    Page<UserMeetingListResponse> getMyMeetingList(String email, Pageable pageable);

    Page<UserPlanListResponse> getUserPlanList(String email, Pageable pageable);

    Page<UserPlanListResponse> getMyPlanList(String email, Pageable pageable);

    Page<UserReviewListResponse> getUserReviewList(String email, Pageable pageable);

    Page<UserPlanReviewableListResponse> getUserPlanListReviewAvailable(String email, Pageable pageable);

    UserPlanListForCalendar getUserPlanListForCalendar(String email, String startDate, String endDate);

}
