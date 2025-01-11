package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.meeting.dto.MeetingCreateRequest;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.user.entity.User;

public interface MeetingStore {

    Meeting storeMeeting(MeetingCreateRequest request, User user);

    void joinMeeting(User user, Meeting meeting);

}
