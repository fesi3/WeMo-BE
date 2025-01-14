package com.wemo.backend.domain.meetingMember.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meetingMember.entity.MeetingMember;
import com.wemo.backend.domain.meetingMember.repository.MeetingMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MeetingMemberReaderImpl implements MeetingMemberReader {

    private final MeetingMemberRepository meetingMemberRepository;

    @Override
    public List<MeetingMember> getMemberListByMeeting(Meeting meeting) {

        return meetingMemberRepository.findAllByMeeting(meeting);

    }

    @Override
    public void delete(MeetingMember meetingMember) {

        meetingMemberRepository.delete(meetingMember);
    }

}
