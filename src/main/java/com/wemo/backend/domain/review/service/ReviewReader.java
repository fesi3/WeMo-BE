package com.wemo.backend.domain.review.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.user.entity.User;

import java.util.List;

public interface ReviewReader {

    List<Review> getReviewByPlan(Plan plan);

    void delete(Review review);

    Review getReview(Long reviewId);

    void validateReview(User user, Review review);

}
