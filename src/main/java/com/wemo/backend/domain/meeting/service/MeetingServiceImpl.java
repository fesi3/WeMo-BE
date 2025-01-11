package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.image.service.ImageStore;
import com.wemo.backend.domain.meeting.dto.MeetingCreateRequest;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final UserReader userReader;
    private final MeetingStore meetingStore;
    private final ImageStore imageStore;
    private final MeetingReader meetingReader;

    /**
     * 0. 모임 생성
     *
     * @param email 이메일
     * @param request 모임 생성 데이터 (모임명, 모임 설명, 카테고리, 모임 이미지)
     */
    @Override
    @Transactional
    public void createMeeting(String email, MeetingCreateRequest request) {

        // 유저 객체 검증
        User userByEmail = userReader.getUserByEmail(email);

        // 모임 객체 저장
        Meeting meeting = meetingStore.storeMeeting(request, userByEmail);

        // 모임 대표 이미지 저장
        imageStore.storeImage(userByEmail, meeting.getId(), request.getFileUrl(), Image.EntityType.MEETING);

        // 모임 생성자는 자동으로 가입
        meetingStore.joinMeeting(userByEmail, meeting);

    }

    /**
     * 모임 가입
     *
     * @param email 이메일
     * @param meetingId 모임 id
     * @return 성공 메세지 반환
     */
    @Override
    @Transactional
    public String joinMeeting(String email, Long meetingId) {

        User user = userReader.getUserByEmail(email);
        Meeting meeting = meetingReader.getMeeting(meetingId);

        meetingStore.joinMeeting(user, meeting);

        return "모임 가입 성공";
    }

}
