package com.wemo.backend.domain.review.service;

import com.wemo.backend.domain.review.dto.ReviewCreateRequest;
import com.wemo.backend.domain.review.dto.ReviewCreateResponse;
import com.wemo.backend.domain.review.dto.ReviewPagingResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ReviewService {

    ReviewCreateResponse createReview(String email, Long planId, ReviewCreateRequest request);

    ReviewPagingResponse getReviewList(Pageable pageable, String province, String district, String startDate, String endDate, Long categoryId, String sort);

    ReviewCreateResponse updateReview(String email, Long reviewId, ReviewCreateRequest request);

    String deleteReview(String email, Long reviewId);

}
