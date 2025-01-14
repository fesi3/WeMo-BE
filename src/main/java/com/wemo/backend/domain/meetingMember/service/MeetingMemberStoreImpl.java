package com.wemo.backend.domain.meetingMember.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meetingMember.entity.MeetingMember;
import com.wemo.backend.domain.meetingMember.repository.MeetingMemberRepository;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.wemo.backend.global.exception.ErrorCode.ALREADY_JOINED_MEETING;

@Service
@RequiredArgsConstructor
public class MeetingMemberStoreImpl implements MeetingMemberStore {

    private final MeetingMemberRepository meetingMemberRepository;

    @Override
    @Transactional
    public void storeMemberToMeeting(User user, Meeting meeting) {

        boolean alreadyJoined = meetingMemberRepository.existsByUserAndMeeting(user, meeting);

        if (alreadyJoined) throw new CustomException(ALREADY_JOINED_MEETING);

        MeetingMember meetingMember = MeetingMember.builder()
                .user(user)
                .meeting(meeting)
                .build();

        meetingMemberRepository.save(meetingMember);
    }

}
