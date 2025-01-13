package com.wemo.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserMeetingListResponse {

    private String email;

    private Long meetingId;

    private String meetingName;

    private String category;

    private Long memberCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
