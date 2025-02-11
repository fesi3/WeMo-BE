package com.wemo.backend.domain.review.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.review.dto.ReviewListResponse;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.wemo.backend.domain.image.entity.QImage.image;
import static com.wemo.backend.domain.plan.entity.QPlan.plan;
import static com.wemo.backend.domain.review.entity.QReview.review;
import static com.wemo.backend.domain.user.entity.QUser.user;

@Slf4j
public class ReviewQueryDslImpl implements ReviewQueryDsl {

    private final JPAQueryFactory queryFactory;

    public ReviewQueryDslImpl(EntityManager em) {

        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ReviewListResponse> getReviewList(Pageable pageable, String province, String district, String startDate, String endDate, Long categoryId, String sort) {

        log.info("전체 후기 목록 조회 요청");

        JPAQuery<ReviewListResponse> queryBuilder = queryFactory
                .select(
                        Projections.constructor(
                                ReviewListResponse.class,
                                review.id,
                                review.score,
                                review.comment,
                                review.createdAt,
                                review.updatedAt,
                                user.nickname,
                                user.profileImagePath,
                                plan.id,
                                plan.planName,
                                Expressions.as(
                                        select(image.fileUrl)
                                                .from(image)
                                                .where(image.entityId.eq(plan.id),
                                                        image.entityType.eq(Image.EntityType.PLAN),
                                                        image.main.eq(true)),
                                        "planImagePath"
                                ),
                                plan.meeting.category.categoryName,
                                plan.address
                        )
                )
                .from(review)
                .leftJoin(review.plan, plan)
                .leftJoin(review.user, user)
                .where(review.plan.meeting.deletedAt.isNull());

        // 필터링 적용
        applyFilters(queryBuilder, province, district, startDate, endDate, categoryId, sort);

        // 페이징 적용
        List<ReviewListResponse> reviewList = queryBuilder
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // 각 리뷰별로 이미지 목록을 별도로 처리
        for (ReviewListResponse review : reviewList) {
            List<String> reviewImageUrls = queryFactory
                    .select(image.fileUrl)
                    .from(image)
                    .where(image.entityId.eq(review.getReviewId()), // reviewId로 이미지를 필터링
                            image.entityType.eq(Image.EntityType.REVIEW))
                    .fetch();

            review.setReviewImages(reviewImageUrls); // 각 리뷰에 해당하는 이미지 목록 설정
        }

        long total = Optional.ofNullable(
                queryFactory
                        .select(review.count())
                        .from(review)
                        .leftJoin(review.plan, plan)
                        .leftJoin(review.user, user)
                        .where(applyFiltersForTotalCount(province, district, startDate, endDate, categoryId)) // 필터를 적용한 메서드 호출
                        .where(review.plan.meeting.deletedAt.isNull())
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(reviewList, pageable, total);
    }

    // 필터링 적용
    private void applyFilters(JPAQuery<ReviewListResponse> queryBuilder, String province, String district, String startDate, String endDate, Long categoryId, String sort) {

        if (province != null && !province.isEmpty()) {
            queryBuilder.where(plan.district.province.provinceName.contains(province));
        }

        if (district != null && !district.isEmpty()) {
            queryBuilder.where(plan.district.districtName.eq(district));
        }

        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            queryBuilder.where(review.createdAt.between(start.atStartOfDay(), end.atTime(23, 59, 59)));
        }

        if (categoryId != null) {
            if (categoryId == 1) {
                queryBuilder.where(plan.meeting.category.parentId.eq(categoryId)); // parentId가 1인 경우
            } else {
                queryBuilder.where(plan.meeting.category.id.eq(categoryId)); // categoryId가 1이 아닌 경우는 id로만 필터링
            }
        }

        queryBuilder.orderBy(getOrderSpecifier(sort));

    }

    private OrderSpecifier<?> getOrderSpecifier(String sort) {

        PathBuilder<?> entityPath = new PathBuilder<>(review.getType(), "review");

        if ("ratingOrder".equals(sort)) {
            return entityPath.getNumber("score", Double.class).desc();
        }
        return entityPath.getDateTime("createdAt", LocalDateTime.class).desc();
    }

    // 필터링된 총 개수를 위한 메서드
    private BooleanBuilder applyFiltersForTotalCount(String province, String district, String startDate, String endDate, Long categoryId) {

        BooleanBuilder builder = new BooleanBuilder();

        if (province != null && !province.isEmpty()) {
            builder.and(plan.district.province.provinceName.contains(province));
        }

        if (district != null && !district.isEmpty()) {
            builder.and(plan.district.districtName.eq(district));
        }

        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            builder.and(review.createdAt.between(start.atStartOfDay(), end.atTime(23, 59, 59)));
        }

        if (categoryId != null) {
            if (categoryId == 1) {
                builder.and(plan.meeting.category.parentId.eq(categoryId)); // parentId가 1인 경우
            } else {
                builder.and(plan.meeting.category.id.eq(categoryId)); // categoryId가 1이 아닌 경우는 id로만 필터링
            }
        }

        return builder;
    }

}
