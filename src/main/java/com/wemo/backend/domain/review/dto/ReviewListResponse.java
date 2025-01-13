package com.wemo.backend.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewListResponse {

    private Long planId;

    private String planName;

    private String planImagePath;

    private String category;

    private String address;

    private String nickname;

    private String profileImagePath;

    private Long reviewId;

    private int score;

    private String comment;

    private String reviewImagePath;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
