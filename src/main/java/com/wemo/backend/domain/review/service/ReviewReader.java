package com.wemo.backend.domain.review.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.review.entity.Review;

import java.util.List;

public interface ReviewReader {

    List<Review> getReviewByPlan(Plan plan);

}
