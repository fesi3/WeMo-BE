package com.wemo.backend.domain.review.service;

import com.wemo.backend.domain.review.dto.ReviewCreateRequest;
import com.wemo.backend.domain.review.dto.ReviewCreateResponse;
import org.springframework.stereotype.Service;

@Service
public interface ReviewService {

    ReviewCreateResponse createReview(String email, Long planId, ReviewCreateRequest request);

}
