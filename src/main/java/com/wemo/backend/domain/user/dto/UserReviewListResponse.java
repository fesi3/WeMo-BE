package com.wemo.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserReviewListResponse {

    private Long reviewId;

    private String planName;

    private LocalDateTime dateTime;

    private String category;

    private String address;

    private int score;

    private String comment;

    private String reviewImagePath;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
