package com.wemo.backend.domain.review.dto;

import com.wemo.backend.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewListInfo {

    private Long reviewId;

    private String nickname;

    private String profileImagePath;

    private int score;

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ReviewListInfo fromEntity(Review review) {

        return ReviewListInfo.builder()
                .reviewId(review.getId())
                .nickname(review.getUser().getNickname())
                .profileImagePath(review.getUser().getProfileImagePath())
                .score(review.getScore())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

}
