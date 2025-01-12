package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meeting.entity.MeetingMember;

import java.util.List;

public interface MeetingReader {

    Meeting getMeeting(Long meetingId);

    List<MeetingMember> getMemberByMeeting(Meeting meeting);

}
