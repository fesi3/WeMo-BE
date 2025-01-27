package com.wemo.backend.domain.review.dto;

import com.wemo.backend.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReviewDetailResponse {

    private String nickname;

    private String profileImagePath;

    private Long reviewId;

    private int score;

    private String comment;

    private List<String> reviewImages;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ReviewDetailResponse fromEntity(Review review, List<String> reviewImageList) {

        return ReviewDetailResponse.builder()
                .nickname(review.getUser().getNickname())
                .profileImagePath(review.getUser().getProfileImagePath())
                .reviewId(review.getId())
                .score(review.getScore())
                .comment(review.getComment())
                .reviewImages(reviewImageList)
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

}
