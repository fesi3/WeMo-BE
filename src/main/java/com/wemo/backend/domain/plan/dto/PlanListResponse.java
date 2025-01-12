package com.wemo.backend.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PlanListResponse {

    private String nickname;

    private String profileImagePath;

    private Long planId;

    private String planName;

    private String category;

    private Long meetingId;

    private String meetingName;

    private String address;

    private String province;

    private String district;

    private String planImagePath;

    private LocalDateTime dateTime;

    private LocalDateTime registrationEnd;

    private int capacity;

    private Long participants;

    private Long likeCount;

    private int viewCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isOpened;

    private boolean isFulled;

    private boolean isLiked;

}
