package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.meeting.dto.MeetingCreateRequest;
import com.wemo.backend.domain.meeting.dto.MeetingDetailResponse;
import com.wemo.backend.domain.meeting.dto.MeetingUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public interface MeetingService {

    void createMeeting(String email, MeetingCreateRequest request);

    String joinMeeting(String email, Long meetingId);

    MeetingDetailResponse getMeetingDetail(Long meetingId);

    String updateMeeting(String email, Long meetingId, MeetingUpdateRequest request);

    String deleteMeeting(String email, Long meetingId);

}
