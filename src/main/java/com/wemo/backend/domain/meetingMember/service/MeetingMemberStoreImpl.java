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

    // 모임 가입
    @Override
    @Transactional
    public void storeMemberToMeeting(User user, Meeting meeting) {
        checkIfAlreadyJoined(user, meeting);
        saveMeetingMember(user, meeting);
    }

    // 모임 미가입 시 일정 참여하는 경우
    @Override
    @Transactional
    public void forceJoinMeeting(User user, Meeting meeting) {
        if (!isAlreadyJoined(user, meeting)) {
            saveMeetingMember(user, meeting);
        }
    }

    // 중복 가입 확인
    private void checkIfAlreadyJoined(User user, Meeting meeting) {
        if (isAlreadyJoined(user, meeting)) {
            throw new CustomException(ALREADY_JOINED_MEETING);
        }
    }

    // 중복 가입 여부 체크
    private boolean isAlreadyJoined(User user, Meeting meeting) {
        return meetingMemberRepository.existsByUserAndMeeting(user, meeting);
    }

    // 모임 회원 저장
    private void saveMeetingMember(User user, Meeting meeting) {
        MeetingMember meetingMember = MeetingMember.builder()
                .user(user)
                .meeting(meeting)
                .build();
        meetingMemberRepository.save(meetingMember);
    }
}
