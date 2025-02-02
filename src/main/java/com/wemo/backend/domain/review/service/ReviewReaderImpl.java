package com.wemo.backend.domain.review.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.review.repository.ReviewRepository;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wemo.backend.global.exception.ErrorCode.REVIEW_NOT_FOUND;
import static com.wemo.backend.global.exception.ErrorCode.REVIEW_PERMISSION_DENIED;

@Component
@RequiredArgsConstructor
public class ReviewReaderImpl implements ReviewReader {

    private final ReviewRepository reviewRepository;

    @Override
    public Review getReview(Long reviewId) {

        return reviewRepository.findByIdAndDeletedAtIsNull(reviewId).orElseThrow(
                () -> new CustomException(REVIEW_NOT_FOUND)
        );
    }

    @Override
    public List<Review> getReviewByPlan(Plan plan) {

        return reviewRepository.findAllByPlan(plan);
    }

    @Override
    public void validateReview(User user, Review review) {

        if (!user.getEmail().equals(review.getUser().getEmail())) throw new CustomException(REVIEW_PERMISSION_DENIED);
    }

    @Override
    public List<Review> getReviewByUser(User user) {

        return reviewRepository.findAllByUserAndDeletedAtIsNull(user);
    }

}
