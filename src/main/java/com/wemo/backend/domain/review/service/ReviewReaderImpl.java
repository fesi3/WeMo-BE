package com.wemo.backend.domain.review.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.review.repository.ReviewRepository;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wemo.backend.global.exception.ErrorCode.ILLEGAL_REVIEW_GRANTED;
import static com.wemo.backend.global.exception.ErrorCode.REVIEW_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class ReviewReaderImpl implements ReviewReader {

    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> getReviewByPlan(Plan plan) {

        return reviewRepository.findAllByPlan(plan);
    }

    @Override
    public void delete(Review review) {

        reviewRepository.delete(review);
    }

    @Override
    public Review getReview(Long reviewId) {

        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(REVIEW_NOT_FOUND)
        );
    }

    @Override
    public void validateReview(User user, Review review) {

        if (!user.getEmail().equals(review.getUser().getEmail())) throw new CustomException(ILLEGAL_REVIEW_GRANTED);
    }

}
