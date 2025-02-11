package com.wemo.backend.domain.user.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.user.dto.*;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.wemo.backend.domain.attendance.entity.QAttendance.attendance;
import static com.wemo.backend.domain.category.entity.QCategory.category;
import static com.wemo.backend.domain.image.entity.QImage.image;
import static com.wemo.backend.domain.like.entity.QLikes.likes;
import static com.wemo.backend.domain.meeting.entity.QMeeting.meeting;
import static com.wemo.backend.domain.meetingMember.entity.QMeetingMember.meetingMember;
import static com.wemo.backend.domain.plan.entity.QPlan.plan;
import static com.wemo.backend.domain.region.entity.QDistrict.district;
import static com.wemo.backend.domain.region.entity.QProvince.province;
import static com.wemo.backend.domain.review.entity.QReview.review;
import static com.wemo.backend.domain.user.entity.QUser.user;

@Slf4j
public class UserQueryDslImpl implements UserQueryDsl {

    private final JPAQueryFactory queryFactory;

    public UserQueryDslImpl(EntityManager em) {

        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<UserMeetingListResponse> getUserMeetingList(String email, Pageable pageable) {

        return getMeetingList(email, pageable, false);
    }

    public Page<UserMeetingListResponse> getMyMeetingList(String email, Pageable pageable) {

        return getMeetingList(email, pageable, true);
    }

    private Page<UserMeetingListResponse> getMeetingList(String email, Pageable pageable, boolean isMyMeeting) {

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(meeting.deletedAt.isNull());
        whereClause.and(user.email.eq(email)); // user 기준으로 필터링
        if (isMyMeeting) {
            log.info("{}가 만든 모임 목록 조회 요청", email);
            whereClause.and(meeting.user.email.eq(email)); // 내가 만든 모임 조회
        } else {
            log.info("{}가 가입된 모임 목록 조회 요청", email);
            whereClause.and(meeting.user.email.ne(email)); // 가입된 모임 중 내가 만든 모임 제외
        }

        List<UserMeetingListResponse> results = queryFactory
                .select(
                        Projections.constructor(
                                UserMeetingListResponse.class,
                                meeting.user.email,
                                meeting.id,
                                meeting.meetingName,
                                Expressions.as(
                                        queryFactory.select(image.fileUrl)
                                                .from(image)
                                                .where(image.entityId.eq(meeting.id),
                                                        image.entityType.eq(Image.EntityType.MEETING),
                                                        image.main.eq(true)),
                                        "meetingImagePath"
                                ),
                                meeting.category.categoryName,
                                Expressions.as(
                                        JPAExpressions.select(meetingMember.count())
                                                .from(meetingMember)
                                                .where(meetingMember.meeting.id.eq(meeting.id)
                                                        .and(meetingMember.deletedAt.isNull())),
                                        "memberCount"
                                ),
                                meeting.createdAt,
                                meeting.updatedAt
                        )
                )
                .from(meetingMember)
                .join(meetingMember.meeting, meeting)
                .join(meetingMember.user, user)
                .where(whereClause)
                .groupBy(meeting.id) // 그룹화
                .orderBy(meeting.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Objects.requireNonNullElse(
                queryFactory.select(meeting.count())
                        .from(meetingMember)
                        .join(meetingMember.meeting, meeting)
                        .join(meetingMember.user, user)
                        .where(whereClause)
                        .fetchOne(), 0L
        );

        return new PageImpl<>(results, pageable, total);
    }

    public Page<UserPlanListResponse> getUserPlanList(String email, Pageable pageable) {

        return getPlanList(email, pageable, false);
    }

    public Page<UserPlanListResponse> getMyPlanList(String email, Pageable pageable) {

        return getPlanList(email, pageable, true);
    }

    private Page<UserPlanListResponse> getPlanList(String email, Pageable pageable, boolean isMyPlan) {

        updateIsFulledStatus();
        LocalDateTime nowKST = LocalDateTime.now(ZoneOffset.ofHours(9));

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(plan.deletedAt.isNull().and(plan.dateTime.after(nowKST)));
        if (isMyPlan) {
            log.info("{}가 만든 일정 목록 조회 요청", email);
            whereClause.and(plan.user.email.eq(email));
        } else {
            log.info("{}가 참여한 일정 목록 조회 요청", email);
            whereClause.and(plan.user.email.ne(email)) // 사용자가 만든 일정이 아니어야 함
                    .and(plan.id.in(
                            JPAExpressions.select(attendance.plan.id)
                                    .from(attendance)
                                    .where(attendance.user.email.eq(email))
                    )); // 사용자가 참여한 일정만 가져오기
        }

        List<UserPlanListResponse> results = queryFactory
                .select(
                        Projections.constructor(
                                UserPlanListResponse.class,
                                user.nickname.as("nickname"),
                                user.email.as("email"),
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
                                        queryFactory.select(province.provinceName)
                                                .from(province)
                                                .where(plan.district.province.id.eq(province.id)),
                                        "province"
                                ),
                                Expressions.as(
                                        queryFactory.select(district.districtName)
                                                .from(district)
                                                .where(plan.district.id.eq(district.id)),
                                        "district"
                                ),
                                Expressions.as(getPlanImageQuery(), "planImagePath"),
                                plan.dateTime,
                                plan.registrationEnd,
                                plan.capacity,
                                Expressions.as(getParticipantCountQuery(), "participants"),
                                Expressions.as(
                                        queryFactory.select(likes.count())
                                                .from(likes)
                                                .where(likes.plan.id.eq(plan.id)),
                                        "likesCount"
                                ),
                                plan.viewCount,
                                plan.createdAt,
                                plan.updatedAt,
                                plan.opened,
                                plan.canceled,
                                Expressions.as(
                                        queryFactory.select(likes.count())
                                                .from(likes)
                                                .where(
                                                        likes.plan.id.eq(plan.id)
                                                                .and(
                                                                        email != null
                                                                                ? likes.user.email.eq(email)
                                                                                : likes.user.email.isNull()
                                                                )
                                                ).gt(0L),
                                        "isLiked")
                        )
                )
                .from(plan)
                .leftJoin(plan.user, user)
                .leftJoin(attendance).on(attendance.plan.eq(plan))
                .where(whereClause)
                .groupBy(plan.id)
                .orderBy(plan.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Objects.requireNonNullElse(
                queryFactory.select(plan.count())
                        .from(plan)
                        .where(whereClause)
                        .fetchOne(), 0L
        );

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<UserReviewListResponse> getUserReviewList(String email, Pageable pageable) {

        log.info("{}의 작성한 후기 목록 조회 요청", email);

        JPAQuery<UserReviewListResponse> queryBuilder = queryFactory
                .select(
                        Projections.constructor(
                                UserReviewListResponse.class,
                                review.id,
                                review.score,
                                review.comment,
                                Expressions.as(getPlanImageQuery(), "planImagePath"),
                                review.createdAt,
                                review.updatedAt,
                                review.plan.id,
                                review.plan.planName,
                                review.plan.dateTime,
                                category.categoryName,
                                review.plan.address
                        )
                )
                .from(review)
                .leftJoin(review.plan, plan)
                .leftJoin(plan.meeting, meeting)
                .leftJoin(meeting.category, category)
                .leftJoin(image).on(image.entityId.eq(review.id)
                        .and(image.entityType.eq(Image.EntityType.REVIEW)))
                .where(review.user.email.eq(email))
                .where(review.plan.meeting.deletedAt.isNull())
                .groupBy(review.id)
                .orderBy(review.createdAt.desc());

        List<UserReviewListResponse> reviewListResponses = queryBuilder
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // 각 리뷰별로 이미지 목록을 별도로 처리
        for (UserReviewListResponse review : reviewListResponses) {
            List<String> reviewImages = queryFactory
                    .select(image.fileUrl)
                    .from(image)
                    .where(image.entityId.eq(review.getReviewId()),
                            image.entityType.eq(Image.EntityType.REVIEW))
                    .fetch();

            review.setReviewImages(reviewImages);
        }

        // 총 개수 조회
        long total = Optional.ofNullable(
                queryFactory
                        .select(review.count())
                        .from(review)
                        .leftJoin(review.plan, plan)
                        .leftJoin(plan.meeting, meeting)
                        .leftJoin(meeting.category, category)
                        .where(review.user.email.eq(email))
                        .where(review.plan.meeting.deletedAt.isNull())
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(reviewListResponses, pageable, total);

    }

    @Override
    public Page<UserPlanReviewableListResponse> getUserPlanListReviewAvailable(String email, Pageable pageable) {

        log.info("{}의 후기 작성 가능한 일정 목록 조회 요청", email);

        LocalDateTime nowKST = LocalDateTime.now(ZoneOffset.ofHours(9));
        BooleanExpression isCompleted = plan.dateTime.before(nowKST);
        BooleanExpression baseCondition = getBaseCondition(email, isCompleted);

        JPAQuery<UserPlanReviewableListResponse> queryBuilder = baseQuery()
                .select(
                        Projections.constructor(
                                UserPlanReviewableListResponse.class,
                                plan.id,
                                plan.planName,
                                plan.dateTime,
                                category.categoryName,
                                plan.address,
                                Expressions.as(getPlanImageQuery(), "planImagePath"),
                                plan.capacity,
                                Expressions.as(getParticipantCountQuery(), "participants"),
                                plan.createdAt,
                                plan.updatedAt
                        )
                )
                .where(baseCondition)
                .groupBy(plan.id)
                .orderBy(plan.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        List<UserPlanReviewableListResponse> reviewListResponses = queryBuilder.fetch();

        long total = Optional.ofNullable(
                baseQuery()
                        .select(plan.count())
                        .where(baseCondition)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(reviewListResponses, pageable, total);
    }

    private BooleanExpression getBaseCondition(String email, BooleanExpression isCompleted) {

        return isCompleted
                .and(attendance.user.email.eq(email))
                .and(attendance.reviewed.eq(false))
                .and(plan.dateTime.before(LocalDateTime.now()))
                .and(plan.deletedAt.isNull())
                .and(plan.meeting.deletedAt.isNull())
                .and(plan.opened.eq(true));
    }

    private JPAQuery<?> baseQuery() {

        return queryFactory
                .from(attendance)
                .leftJoin(attendance.plan, plan)
                .leftJoin(plan.meeting, meeting)
                .leftJoin(meeting.category, category)
                .leftJoin(attendance.user, user)
                .leftJoin(review).on(review.plan.id.eq(plan.id));
    }

    @Override
    public UserPlanListForCalendar getUserPlanListForCalendar(String email, String startDate, String endDate) {

        log.info("{}의 월별 일정 목록 조회 요청", email);
        updateIsFulledStatus();

        LocalDateTime nowKST = LocalDateTime.now(ZoneOffset.ofHours(9));
        BooleanExpression isCompleted = plan.dateTime.before(nowKST);

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(plan.deletedAt.isNull()); // 일정이 삭제되지 않은 것
        whereClause.and(plan.opened.eq(true).or(isCompleted)); // 개설 확정이거나 이용 완료된 일정만 조회
        whereClause.and(
                plan.user.email.eq(email) // 유저가 생성한 일정
                        .or(plan.id.in( // 또는 유저가 참여한 일정
                                JPAExpressions.select(attendance.plan.id)
                                        .from(attendance)
                                        .where(attendance.user.email.eq(email))
                        ))
        );
        whereClause.and(buildFilterConditions(startDate, endDate)); // 기간 조건 추가

        List<UserPlanListResponseForCalendar> planListForCalendar = queryFactory
                .select(
                        Projections.constructor(
                                UserPlanListResponseForCalendar.class,
                                plan.id,
                                plan.planName,
                                Expressions.as(getPlanImageQuery(), "planImagePath"),
                                plan.dateTime,
                                category.categoryName,
                                plan.addressDetail,
                                plan.createdAt,
                                plan.updatedAt,
                                plan.opened,
                                isCompleted
                        )
                )
                .from(plan)
                .leftJoin(plan.meeting, meeting)
                .leftJoin(meeting.category, category)
                .where(whereClause)
                .orderBy(plan.dateTime.asc())
                .stream().toList();

        return new UserPlanListForCalendar(planListForCalendar);
    }

    private BooleanExpression buildFilterConditions(String startDate, String endDate) {

        BooleanExpression condition = plan.canceled.eq(false);

        condition = buildDateFilter(startDate, endDate, condition);

        return condition;
    }

    public static BooleanExpression buildDateFilter(String startDate, String endDate, BooleanExpression condition) {

        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            condition = condition.and(plan.dateTime.between(start.atStartOfDay(), end.atTime(23, 59, 59)));
        }
        return condition;
    }

    private void updateIsFulledStatus() {

        long updatedCount = queryFactory.update(plan)
                .set(plan.fulled, true)
                .where(plan.registrationEnd.before(LocalDateTime.now())
                        .and(plan.fulled.eq(false)))
                .execute();

        log.info("일정의 모집 마감 상태가 {} 번 업데이트되었습니다.", updatedCount);

    }

    private JPQLQuery<String> getPlanImageQuery() {

        return queryFactory.select(image.fileUrl)
                .from(image)
                .where(image.entityId.eq(plan.id),
                        image.entityType.eq(Image.EntityType.PLAN),
                        image.main.eq(true));
    }

    private JPQLQuery<Long> getParticipantCountQuery() {

        return queryFactory.select(attendance.count())
                .from(attendance)
                .where(attendance.plan.id.eq(plan.id)
                        .and(attendance.deletedAt.isNull()));
    }

}
