package com.wemo.backend.domain.review.repository.querydsl;

import com.wemo.backend.domain.review.dto.ReviewListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewQueryDsl {

    Page<ReviewListResponse> getReviewList(Pageable pageable, String province, String district, String startDate, String endDate, Long categoryId, String sort);

}
