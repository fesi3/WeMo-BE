package com.wemo.backend.domain.review.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.review.dto.ReviewCreateRequest;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.user.entity.User;

public interface ReviewStore {

    Review storeReview(ReviewCreateRequest request, User user, Plan plan);

    void deleteReview(Review review);

}
