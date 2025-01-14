package com.wemo.backend.domain.user.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.user.dto.UserMeetingListResponse;
import com.wemo.backend.domain.user.dto.UserPlanListResponse;
import com.wemo.backend.domain.user.dto.UserPlanReviewableListResponse;
import com.wemo.backend.domain.user.dto.UserReviewListResponse;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
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

        // 메인 쿼리
        JPAQuery<UserMeetingListResponse> queryBuilder = queryFactory
                .select(
                        Projections.constructor(
                                UserMeetingListResponse.class,
                                meeting.user.email,
                                meeting.id,
                                meeting.meetingName,
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
                .from(meeting)
                .leftJoin(meeting.user, user)
                .leftJoin(meetingMember).on(meetingMember.meeting.eq(meeting))
                .where(meeting.user.email.eq(email) // 유저가 작성한 모임
                        .or(meetingMember.user.email.eq(email))) // 유저가 가입한 모임
                .where(meeting.deletedAt.isNull())
                .groupBy(meeting.id) // 그룹화
                .orderBy(meeting.createdAt.desc());

        // 페이징 처리
        List<UserMeetingListResponse> userMeetingListResponses = queryBuilder
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // 총 개수 조회
        long total = Optional.ofNullable(
                queryFactory
                .select(meeting.count())
                .from(meeting)
                .leftJoin(meetingMember).on(meetingMember.meeting.eq(meeting))
                .where(meeting.user.email.eq(email)
                    .or(meetingMember.user.email.eq(email)))
                .where(meeting.deletedAt.isNull())
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(userMeetingListResponses, pageable, total);
    }

    @Override
    public Page<UserPlanListResponse> getUserPlanList(String email, Pageable pageable) {

        updateIsFulledStatus();

        JPAQuery<UserPlanListResponse> queryBuilder = queryFactory
                .select(
                        Projections.constructor(
                                UserPlanListResponse.class,
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
                                        "likesCount"
                                ),
                                plan.viewCount,
                                plan.createdAt,
                                plan.updatedAt,
                                plan.opened,
                                plan.canceled,
                                plan.fulled,
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
                                        "islikesd")
                        )
                )
                .from(plan)
                .leftJoin(plan.user, user)
                .leftJoin(attendance).on(attendance.plan.eq(plan))
                .where(attendance.user.email.eq(email)) // 유저가 참여한 일정
                .where(plan.meeting.deletedAt.isNull())
                .orderBy(plan.createdAt.desc());

        // 페이징 처리
        List<UserPlanListResponse> planListResponses = queryBuilder
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // 총 개수 조회
        long total = Optional.ofNullable(
                queryFactory
                        .select(plan.count())
                        .from(plan)
                        .leftJoin(attendance).on(attendance.plan.eq(plan))
                        .where(attendance.user.email.eq(email))
                        .where(plan.meeting.deletedAt.isNull())
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(planListResponses, pageable, total);

    }

    @Override
    public Page<UserReviewListResponse> getUserReviewList(String email, Pageable pageable) {

        JPAQuery<UserReviewListResponse> queryBuilder = queryFactory
                .select(
                        Projections.constructor(
                                UserReviewListResponse.class,
                                review.id,
                                review.plan.planName,
                                review.plan.dateTime,
                                category.categoryName,
                                review.plan.address,
                                review.score,
                                review.comment,
                                Expressions.as(
                                        queryFactory.select(image.fileUrl)
                                                .from(image)
                                                .where(image.entityId.eq(review.id),
                                                        image.entityType.eq(Image.EntityType.REVIEW),
                                                        image.main.eq(true)),
                                        "reviewImagePath"
                                ),
                                review.createdAt,
                                review.updatedAt
                        )
                )
                .from(review)
                .leftJoin(review.plan, plan)
                .leftJoin(plan.meeting, meeting)  // meeting과 조인
                .leftJoin(meeting.category, category)  // category와 조인
                .leftJoin(image).on(image.entityId.eq(review.id) // Image 엔티티와 review.id를 조인
                        .and(image.entityType.eq(Image.EntityType.REVIEW)))
                .where(review.user.email.eq(email)) // 유저가 작성한 후기만
                .where(review.plan.meeting.deletedAt.isNull())
                .orderBy(review.createdAt.desc());

        List<UserReviewListResponse> reviewListResponses = queryBuilder
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

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

        JPAQuery<UserPlanReviewableListResponse> queryBuilder = queryFactory
                .select(
                        Projections.constructor(
                                UserPlanReviewableListResponse.class,
                                plan.id,
                                plan.planName,
                                plan.dateTime,
                                category.categoryName,
                                plan.address,
                                Expressions.as(
                                        queryFactory.select(image.fileUrl)
                                                .from(image)
                                                .where(image.entityId.eq(plan.id),
                                                        image.entityType.eq(Image.EntityType.PLAN),
                                                        image.main.eq(true)),
                                        "planImagePath"
                                ),
                                plan.capacity,
                                Expressions.as(
                                        queryFactory.select(attendance.count())
                                                .from(attendance)
                                                .where(attendance.plan.id.eq(plan.id)
                                                        .and(attendance.deletedAt.isNull())),
                                        "participants"
                                ),
                                plan.createdAt,
                                plan.updatedAt
                        )
                )
                .from(attendance) // attendance 테이블을 from 절에서 시작
                .leftJoin(attendance.plan, plan) // plan과 조인
                .leftJoin(plan.meeting, meeting)
                .leftJoin(meeting.category, category)
                .leftJoin(attendance.user, user) // user와 조인
                .where(
                        attendance.user.email.eq(email) // 이메일 조건
                                .and(attendance.reviewed.eq(false)) // 후기 미작성
                                .and(plan.dateTime.before(LocalDateTime.now())) // 일정이 지남
                )
                .where(review.plan.meeting.deletedAt.isNull())
                .orderBy(plan.createdAt.desc());

        List<UserPlanReviewableListResponse> reviewListResponses = queryBuilder
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // 총 개수 조회
        long total = Optional.ofNullable(
                queryFactory
                        .select(plan.count())
                        .from(attendance) // attendance 테이블을 from 절에서 시작
                        .leftJoin(attendance.plan, plan) // plan과 조인
                        .leftJoin(plan.meeting, meeting)
                        .leftJoin(meeting.category, category)
                        .leftJoin(attendance.user, user) // user와 조인
                        .where(
                                attendance.user.email.eq(email) // 이메일 조건
                                        .and(attendance.reviewed.eq(false)) // 후기 미작성
                                        .and(plan.dateTime.before(LocalDateTime.now())) // 일정이 지남
                        )
                        .where(review.plan.meeting.deletedAt.isNull())
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(reviewListResponses, pageable, total);
    }

    private void updateIsFulledStatus() {

        long updatedCount = queryFactory.update(plan)
                .set(plan.fulled, true)
                .where(plan.registrationEnd.before(LocalDateTime.now())
                        .and(plan.fulled.eq(false)))
                .execute();

        log.info("일정의 모집 마감 상태가 {} 번 업데이트되었습니다.", updatedCount);

    }

}
