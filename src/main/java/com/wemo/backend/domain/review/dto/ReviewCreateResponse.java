package com.wemo.backend.domain.review.dto;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewCreateResponse {

    private Long planId;

    private Long reviewId;

    private int score;

    private String comment;

    private List<String> reviewImagePath;

    // 이미지가 있는 경우
    public static ReviewCreateResponse fromEntityWithImage(Plan plan, Review review, List<String> reviewImageList) {

        return ReviewCreateResponse.builder()
                .planId(plan.getId())
                .reviewId(review.getId())
                .score(review.getScore())
                .comment(review.getComment())
                .reviewImagePath(reviewImageList)
                .build();
    }


    // 이미지가 없는 경우
    public static ReviewCreateResponse fromEntity(Plan plan, Review review) {

        return ReviewCreateResponse.builder()
                .planId(plan.getId())
                .reviewId(review.getId())
                .score(review.getScore())
                .comment(review.getComment())
                .build();
    }

}
