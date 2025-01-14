package com.wemo.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserPlanReviewableListResponse {

    private Long planId;

    private String planName;

    private LocalDateTime dateTime;

    private String category;

    private String address;

    private String planImagePath;

    private int capacity;

    private Long participants;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
