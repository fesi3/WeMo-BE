package com.wemo.backend.domain.review.dto;

import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewCreateResponse {

    private Long planId;

    private int score;

    private String comment;

    private String reviewImagePath;

    // 이미지가 있는 경우
    public static ReviewCreateResponse fromEntityWithImage(Plan plan, Review review, Image image) {
        return ReviewCreateResponse.builder()
                .planId(plan.getId())
                .score(review.getScore())
                .comment(review.getComment())
                .reviewImagePath(image.getFileUrl())
                .build();
    }


    // 이미지가 없는 경우
    public static ReviewCreateResponse fromEntity(Plan plan, Review review) {
        return ReviewCreateResponse.builder()
                .planId(plan.getId())
                .score(review.getScore())
                .comment(review.getComment())
                .build();
    }

}
