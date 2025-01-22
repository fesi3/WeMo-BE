package com.wemo.backend.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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

    private List<String> reviewImages;  // List<String> reviewImages 추가

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ReviewListResponse의 생성자를 추가하여 reviewImages 필드를 처리하도록 수정
    public ReviewListResponse(Long planId, String planName, String planImagePath, String category, String address,
                              String nickname, String profileImagePath, Long reviewId, int score, String comment,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.planId = planId;
        this.planName = planName;
        this.planImagePath = planImagePath;
        this.category = category;
        this.address = address;
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
        this.reviewId = reviewId;
        this.score = score;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // reviewImages는 setter로 설정
    public void setReviewImages(List<String> reviewImages) {

        this.reviewImages = reviewImages;
    }

}
