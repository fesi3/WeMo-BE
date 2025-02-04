package com.wemo.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserReviewListResponse {

    private Long reviewId;

    private int score;

    private String comment;

    private String planImagePath;

    private List<String> reviewImages;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long planId;

    private String planName;

    private LocalDateTime dateTime;

    private String category;

    private String address;

    public UserReviewListResponse(Long reviewId, int score, String comment, String planImagePath, LocalDateTime createdAt, LocalDateTime updatedAt, Long planId, String planName, LocalDateTime dateTime, String category, String address) {

        this.reviewId = reviewId;
        this.score = score;
        this.comment = comment;
        this.planImagePath = planImagePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.planId = planId;
        this.planName = planName;
        this.dateTime = dateTime;
        this.category = category;
        this.address = address;
    }

    public void setReviewImages(List<String> reviewImages) {

        this.reviewImages = reviewImages;
    }

}
