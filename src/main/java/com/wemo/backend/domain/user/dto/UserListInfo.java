package com.wemo.backend.domain.user.dto;

import com.wemo.backend.domain.meeting.entity.MeetingMember;
import com.wemo.backend.domain.plan.entity.Attendance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserListInfo {

    private String nickname;

    private String profileImagePath;

    private LocalDateTime createdAt;

    public static UserListInfo fromEntity(MeetingMember meetingMember) {

        return UserListInfo.builder()
                .nickname(meetingMember.getUser().getNickname())
                .profileImagePath(meetingMember.getUser().getProfileImagePath())
                .createdAt(meetingMember.getCreatedAt())
                .build();
    }

    public static UserListInfo fromEntity(Attendance attendance) {

        return UserListInfo.builder()
                .nickname(attendance.getUser().getNickname())
                .profileImagePath(attendance.getUser().getProfileImagePath())
                .createdAt(attendance.getCreatedAt())
                .build();
    }

}
