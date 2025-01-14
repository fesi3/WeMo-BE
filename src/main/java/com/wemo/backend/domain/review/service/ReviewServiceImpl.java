package com.wemo.backend.domain.review.service;

import com.wemo.backend.domain.attendance.entity.Attendance;
import com.wemo.backend.domain.attendance.service.AttendanceReader;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.image.service.ImageStore;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.service.PlanReader;
import com.wemo.backend.domain.review.dto.ReviewCreateRequest;
import com.wemo.backend.domain.review.dto.ReviewCreateResponse;
import com.wemo.backend.domain.review.dto.ReviewPagingResponse;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.review.repository.ReviewRepository;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.wemo.backend.global.exception.ErrorCode.REVIEW_ALREADY_EXISTS;
import static com.wemo.backend.global.exception.ErrorCode.REVIEW_CREATION_BEFORE_PLAN_END;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserReader userReader;

    private final PlanReader planReader;

    private final ReviewRepository reviewRepository;

    private final ReviewStore reviewStore;

    private final ImageStore imageStore;

    private final AttendanceReader attendanceReader;

    private final ReviewReader reviewReader;

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

        // 일정이 완료되었는지 확인
        if (plan.getDateTime().isAfter(LocalDateTime.now())) {
            throw new CustomException(REVIEW_CREATION_BEFORE_PLAN_END);
        }

        // 참여 내역 검사 후 상태값 변경
        Attendance attendance = attendanceReader.validateAttendance(user, plan);
        attendance.updateStatus();

        // 후기 저장
        Review review = reviewStore.storeReview(request, user, plan);

        // 이미지 저장 (선택)
        if (request.getFileUrls() != null || !request.getFileUrls().isEmpty()) {
            List<String> imageList = imageStore.storeImageList(user, review.getId(), request.getFileUrls(), Image.EntityType.REVIEW);
            return ReviewCreateResponse.fromEntityWithImage(plan, review, imageList);
        }
        return ReviewCreateResponse.fromEntity(plan, review);

    }

    /**
     * 전체 후기 목록 조회 (페이지 기반 페이징처리)
     *
     * @param pageable 페이징 관련 데이터
     * @param province 시/도
     * @param district 군/구
     * @param startDate 필터링 시작날짜
     * @param endDate 필터링 끝날짜
     * @param categoryId 카테고리 id
     * @param sort 정렬 기준
     * @return 페이징 처리된 후기 목록
     */
    @Override
    public ReviewPagingResponse getReviewList(Pageable pageable, String province, String district, String startDate, String endDate, Long categoryId, String sort) {

        return new ReviewPagingResponse(reviewRepository.getReviewList(pageable, province, district, startDate, endDate, categoryId, sort));
    }

    /**
     * 후기 수정
     *
     * @param email 이메일
     * @param reviewId 후기 id
     * @param request 후기 수정 데이터 (평점, 내용, 이미지)
     * @return 수정된 후기 정보
     */
    @Override
    @Transactional
    public ReviewCreateResponse updateReview(String email, Long reviewId, ReviewCreateRequest request) {

        User user = userReader.getUserByEmail(email);
        Review review = reviewReader.getReview(reviewId);
        // 본인 후기인지 확인
        reviewReader.validateReview(user, review);

        Review updatedReview = review.update(request);
        Plan plan = updatedReview.getPlan();

        // 이미지 수정 (선택)
        if (request.getFileUrls() != null || !request.getFileUrls().isEmpty()) {
            imageStore.updateImage(user, reviewId, request.getFileUrls(), Image.EntityType.REVIEW);
            return ReviewCreateResponse.fromEntityWithImage(plan, updatedReview, request.getFileUrls());
        }
        return ReviewCreateResponse.fromEntity(plan, updatedReview);
    }

}
