package com.wemo.backend.domain.meetingMember.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface MeetingMemberStore {

    void storeMemberToMeeting(User user, Meeting meeting);

    void forceJoinMeeting(User user, Meeting meeting);

    void deleteMember(User user, Meeting meeting);

    boolean isAlreadyJoined(User user, Meeting meeting);

}
