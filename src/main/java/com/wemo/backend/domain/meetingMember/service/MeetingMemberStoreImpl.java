package com.wemo.backend.domain.meetingMember.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meetingMember.entity.MeetingMember;
import com.wemo.backend.domain.meetingMember.repository.MeetingMemberRepository;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.wemo.backend.global.exception.ErrorCode.*;

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

    @Override
    public void deleteMember(User user, Meeting meeting) {
        // 모임장인 경우 탈퇴 불가능
        if (meeting.getUser().getEmail().equals(user.getEmail())) throw new CustomException(GROUP_LEADER_CANNOT_LEAVE);
        // 가입 여부 판단 후 삭제
        MeetingMember byUserAndMeeting = meetingMemberRepository.findByUserAndMeeting(user, meeting).orElseThrow(
                () -> new CustomException(MEETING_MEMBER_NOT_FOUND)
        );

        meetingMemberRepository.delete(byUserAndMeeting);
    }

    // 중복 가입 확인
    private void checkIfAlreadyJoined(User user, Meeting meeting) {

        if (isAlreadyJoined(user, meeting)) {
            throw new CustomException(ALREADY_JOINED_MEETING);
        }
    }

    // 가입 여부 체크
    public boolean isAlreadyJoined(User user, Meeting meeting) {

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
