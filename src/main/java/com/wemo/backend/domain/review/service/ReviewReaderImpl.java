package com.wemo.backend.domain.review.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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

}
