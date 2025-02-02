package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meeting.repository.MeetingRepository;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wemo.backend.global.exception.ErrorCode.MEETING_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class MeetingReaderImpl implements MeetingReader {

    private final MeetingRepository meetingRepository;

    @Override
    public Meeting getMeeting(Long meetingId) {

        return meetingRepository.findById(meetingId).orElseThrow(
                () -> new CustomException(MEETING_NOT_FOUND)
        );
    }

    @Override
    public List<Meeting> getMeetingListByUser(User user) {

        return meetingRepository.findAllByUserAndDeletedAtIsNull(user);
    }

}
