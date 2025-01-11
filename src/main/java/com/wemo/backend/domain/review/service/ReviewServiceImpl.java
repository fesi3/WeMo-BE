package com.wemo.backend.domain.review.service;

import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.image.service.ImageStore;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.service.PlanReader;
import com.wemo.backend.domain.review.dto.ReviewCreateRequest;
import com.wemo.backend.domain.review.dto.ReviewCreateResponse;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.review.repository.ReviewRepository;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.wemo.backend.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserReader userReader;

    private final PlanReader planReader;

    private final ReviewRepository reviewRepository;

    private final ReviewStore reviewStore;

    private final ImageStore imageStore;

    /**
     * 후기 등록
     *
     * @param email 이메일
     * @param planId 일정 id
     * @param request 후기 생성 데이터 (평점, 내용, 이미지)
     * @return 생성된 후기 정보
     */
    @Override
    @Transactional
    public ReviewCreateResponse createReview(String email, Long planId, ReviewCreateRequest request) {

        // 유저 유효성 검사
        User user = userReader.getUserByEmail(email);
        // 일정 유효성 검사
        Plan plan = planReader.getPlan(planId);

        // 기존 후기 작성 여부 확인
        boolean hasExistingReview = reviewRepository.existsByUserAndPlan(user, plan);
        if (hasExistingReview) {
            throw new CustomException(REVIEW_ALREADY_EXISTS);
        }

        // 참여 내역 검사
        planReader.validateAttendance(user, plan);

        // 일정이 완료되었는지 확인
        if (plan.getDateTime().isAfter(LocalDateTime.now())) {
            throw new CustomException(REVIEW_CREATION_BEFORE_PLAN_END);
        }

        // 후기 저장
        Review review = reviewStore.storeReview(request, user, plan);

        // 이미지 저장 (선택)
        if (!request.getFileUrl().isEmpty()) {
            Image image = imageStore.storeImage(user, review.getId(), request.getFileUrl(), Image.EntityType.REVIEW);
            return ReviewCreateResponse.fromEntityWithImage(plan, review, image);
        }
        return ReviewCreateResponse.fromEntity(plan, review);

    }

}
