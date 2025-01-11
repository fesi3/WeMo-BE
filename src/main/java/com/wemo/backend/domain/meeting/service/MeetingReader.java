package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.meeting.entity.Meeting;

public interface MeetingReader {

    Meeting getMeeting(Long meetingId);

}
