package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.category.entity.Category;
import com.wemo.backend.domain.category.repository.CategoryRepository;
import com.wemo.backend.domain.meeting.dto.MeetingCreateRequest;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meeting.entity.MeetingMember;
import com.wemo.backend.domain.meeting.repository.MeetingMemberRepository;
import com.wemo.backend.domain.meeting.repository.MeetingRepository;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.wemo.backend.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class MeetingStoreImpl implements MeetingStore {

    private final CategoryRepository categoryRepository;

    private final MeetingRepository meetingRepository;

    private final MeetingMemberRepository meetingMemberRepository;

    @Override
    @Transactional
    public Meeting storeMeeting(MeetingCreateRequest request, User user) {

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new CustomException(ILLEGAL_CATEGORY_NOT_FOUND)
        );

        Meeting meeting = Meeting.builder()
                .meetingName(request.getMeetingName())
                .description(request.getDescription())
                .category(category)
                .user(user)
                .build();

        meetingRepository.save(meeting);

        return meeting;
    }

    @Override
    @Transactional
    public void joinMeeting(User user, Meeting meeting) {

        boolean alreadyJoined = meetingMemberRepository.existsByUserAndMeeting(user, meeting);

        if (alreadyJoined) throw new CustomException(ALREADY_JOINED_MEETING);

        MeetingMember meetingMember = MeetingMember.builder()
                .user(user)
                .meeting(meeting)
                .build();

        meetingMemberRepository.save(meetingMember);
    }

}
