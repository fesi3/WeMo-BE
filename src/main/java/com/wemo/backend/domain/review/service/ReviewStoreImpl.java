package com.wemo.backend.domain.review.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.review.dto.ReviewCreateRequest;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.review.repository.ReviewRepository;
import com.wemo.backend.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewStoreImpl implements ReviewStore {

    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public Review storeReview(ReviewCreateRequest request, User user, Plan plan) {

        Review review = Review.builder()
                .score(request.getScore())
                .comment(request.getComment())
                .user(user)
                .plan(plan)
                .build();

        reviewRepository.save(review);

        return review;
    }

    @Override
    public void deleteReview(Review review) {

        reviewRepository.delete(review);
    }

}
