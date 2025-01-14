package com.wemo.backend.domain.plan.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.plan.dto.PlanListResponse;
import com.wemo.backend.domain.region.entity.QDistrict;
import com.wemo.backend.domain.region.entity.QProvince;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.wemo.backend.domain.attendance.entity.QAttendance.attendance;
import static com.wemo.backend.domain.image.entity.QImage.image;
import static com.wemo.backend.domain.like.entity.QLikes.likes;
import static com.wemo.backend.domain.meeting.entity.QMeeting.meeting;
import static com.wemo.backend.domain.plan.entity.QPlan.plan;
import static com.wemo.backend.domain.plan.repository.querydsl.PlanCursorQueryDslImpl.getBooleanExpression;
import static com.wemo.backend.domain.user.entity.QUser.user;

@Slf4j
public class PlanQueryDslImpl implements PlanQueryDsl {

    private final JPAQueryFactory queryFactory;

    public PlanQueryDslImpl(EntityManager em) {

        this.queryFactory = new JPAQueryFactory(em);
    }

    private static final String DEFAULT_SORT_FIELD = "createdAt";

    @Override
    public Page<PlanListResponse> getLikedPlanList(String email, Pageable pageable, String query, String province, String district, String startDate, String endDate, Long categoryId, String sort) {

        updateIsFulledStatus();

        log.info("{}가 좋아요한 일정 목록 조회 요청", email);

        String sortField = (sort == null) ? DEFAULT_SORT_FIELD : sort;

        BooleanBuilder baseConditions = new BooleanBuilder()
                .and(buildFilterConditions(query, province, district, startDate, endDate, categoryId));

        JPAQuery<PlanListResponse> queryBuilder = queryFactory
                .select(
                        Projections.constructor(
                                PlanListResponse.class,
                                user.nickname.as("nickname"),
                                user.profileImagePath.as("profileImage"),
                                plan.id.as("planId"),
                                plan.planName,
                                Expressions.as(
                                        queryFactory.select(meeting.category.categoryName)
                                                .from(meeting)
                                                .where(meeting.id.eq(plan.meeting.id)),
                                        "category"
                                ),
                                Expressions.as(
                                        queryFactory.select(meeting.id)
                                                .from(meeting)
                                                .where(meeting.id.eq(plan.meeting.id)),
                                        "meetingId"
                                ),
                                Expressions.as(
                                        queryFactory.select(meeting.meetingName)
                                                .from(meeting)
                                                .where(meeting.id.eq(plan.meeting.id)),
                                        "meetingName"
                                ),
                                plan.address,
                                Expressions.as(
                                        queryFactory.select(QProvince.province.provinceName)
                                                .from(QProvince.province)
                                                .where(plan.district.province.id.eq(QProvince.province.id)),
                                        "province"
                                ),
                                Expressions.as(
                                        queryFactory.select(QDistrict.district.districtName)
                                                .from(QDistrict.district)
                                                .where(plan.district.id.eq(QDistrict.district.id)),
                                        "district"
                                ),
                                Expressions.as(
                                        queryFactory.select(image.fileUrl)
                                                .from(image)
                                                .where(image.entityId.eq(plan.id),
                                                        image.entityType.eq(Image.EntityType.PLAN),
                                                        image.main.eq(true)),
                                        "planImagePath"
                                ),
                                plan.dateTime,
                                plan.registrationEnd,
                                plan.capacity,
                                Expressions.as(
                                        queryFactory.select(attendance.count())
                                                .from(attendance)
                                                .where(attendance.plan.id.eq(plan.id)
                                                        .and(attendance.deletedAt.isNull())),
                                        "participants"
                                ),
                                Expressions.as(
                                        queryFactory.select(likes.count())
                                                .from(likes)
                                                .where(likes.plan.id.eq(plan.id)),
                                        "likeCount"
                                ),
                                plan.viewCount,
                                plan.createdAt,
                                plan.updatedAt,
                                plan.opened,
                                plan.fulled,
                                Expressions.as(
                                        queryFactory.select(likes.count())
                                                .from(likes)
                                                .where(
                                                        likes.plan.id.eq(plan.id)
                                                                .and(likes.user.email.eq(email))
                                                ).gt(0L),
                                        "isLiked")
                        )
                )
                .from(plan)
                .leftJoin(plan.user, user)
                .leftJoin(likes).on(likes.plan.id.eq(plan.id))
                .where(baseConditions)
                .where(likes.user.email.eq(email)) // 유저가 좋아요한 일정
                .where(plan.canceled.eq(false))
                .where(plan.meeting.deletedAt.isNull())
                .orderBy(sortField.equals("closeDate") ? plan.registrationEnd.asc() : plan.createdAt.desc());

        List<PlanListResponse> planListResponses = queryBuilder
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        long total = Optional.ofNullable(
                queryFactory
                        .select(plan.count())
                        .from(plan)
                        .leftJoin(plan.user, user)
                        .leftJoin(likes).on(likes.plan.id.eq(plan.id))
                        .where(baseConditions)
                        .where(likes.user.email.eq(email)) // 유저가 좋아요한 일정
                        .where(plan.canceled.eq(false))
                        .where(plan.meeting.deletedAt.isNull())
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(planListResponses, pageable, total);

    }

    private void updateIsFulledStatus() {

        long updatedCount = queryFactory.update(plan)
                .set(plan.fulled, true)
                .where(plan.registrationEnd.before(LocalDateTime.now())
                        .and(plan.fulled.eq(false)))
                .execute();

        log.info("일정의 모집 마감 상태가 {} 번 업데이트되었습니다.", updatedCount);

    }

    private BooleanExpression buildFilterConditions(String query, String province, String district, String startDate, String endDate, Long categoryId) {

        return getBooleanExpression(query, province, district, startDate, endDate, categoryId);
    }

}
