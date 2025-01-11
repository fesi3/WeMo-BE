package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meeting.repository.MeetingRepository;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.wemo.backend.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class MeetingReaderImpl implements MeetingReader {

    private final MeetingRepository meetingRepository;

    public Meeting getMeeting(Long meetingId) {
        return meetingRepository.findById(meetingId).orElseThrow(
                () -> new CustomException(ILLEGAL_MEETING_NOT_FOUND)
        );
    }
}
