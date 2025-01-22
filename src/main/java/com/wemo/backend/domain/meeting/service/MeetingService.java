package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.meeting.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface MeetingService {

    MeetingCreateResponse createMeeting(String email, MeetingCreateRequest request);

    String joinMeeting(String email, Long meetingId);

    MeetingDetailResponse getMeetingDetail(Long meetingId);

    String updateMeeting(String email, Long meetingId, MeetingUpdateRequest request);

    String deleteMeeting(String email, Long meetingId);

    MeetingMemberPagingResponse getMemberListByMeeting(Long meetingId, Pageable pageable);

    MeetingPlanPagingResponse getPlanListByMeeting(Long meetingId, Pageable pageable);

    MeetingReviewPagingResponse getReviewListByMeeting(Long meetingId, Pageable pageable);

}
