package com.wemo.backend.domain.meetingMember.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meetingMember.entity.MeetingMember;

import java.util.List;

public interface MeetingMemberReader {

    List<MeetingMember> getMemberListByMeeting(Meeting meeting);

    void delete(MeetingMember meetingMember);

}
