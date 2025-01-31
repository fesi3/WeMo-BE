package com.wemo.backend.domain.meeting.repository;

import com.wemo.backend.domain.meeting.dto.MeetingCursorPagingResponse;
import com.wemo.backend.domain.meeting.dto.MeetingPlanListResponse;
import com.wemo.backend.domain.meeting.dto.MeetingReviewListResponse;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.user.dto.UserListInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingQueryDsl {

    Page<UserListInfo> getMemberListByMeeting(Meeting meeting, Pageable pageable);

    Page<MeetingPlanListResponse> getPlanListByMeeting(Meeting meeting, Pageable pageable);

    Page<MeetingReviewListResponse> getReviewListByMeeting(Meeting meeting, Pageable pageable);

    MeetingCursorPagingResponse getMeetingList(Long cursor, int size, Long categoryId, String sort);

}
