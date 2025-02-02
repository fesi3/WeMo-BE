package com.wemo.backend.domain.meetingMember.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meetingMember.entity.MeetingMember;
import com.wemo.backend.domain.user.entity.User;

import java.util.List;

public interface MeetingMemberReader {

    List<MeetingMember> getMemberListByMeeting(Meeting meeting);


    List<MeetingMember> getJoinedMeetingsByUser(User user);

}
