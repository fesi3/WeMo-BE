package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meetingMember.entity.MeetingMember;

import java.util.List;

public interface MeetingReader {

    Meeting getMeeting(Long meetingId);

}
