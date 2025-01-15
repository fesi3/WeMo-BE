package com.wemo.backend.domain.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MeetingReviewListResponse {

    private String nickname;

    private String profileImagePath;

    private int score;

    private String comment;

    private String reviewImagePath;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
