package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meeting.entity.MeetingMember;
import com.wemo.backend.domain.meeting.repository.MeetingMemberRepository;
import com.wemo.backend.domain.meeting.repository.MeetingRepository;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wemo.backend.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class MeetingReaderImpl implements MeetingReader {

    private final MeetingRepository meetingRepository;

    private final MeetingMemberRepository meetingMemberRepository;

    public Meeting getMeeting(Long meetingId) {
        return meetingRepository.findById(meetingId).orElseThrow(
                () -> new CustomException(ILLEGAL_MEETING_NOT_FOUND)
        );
    }

    @Override
    public List<MeetingMember> getMemberByMeeting(Meeting meeting) {

        return meetingMemberRepository.findAllByMeeting(meeting);

    }

}
