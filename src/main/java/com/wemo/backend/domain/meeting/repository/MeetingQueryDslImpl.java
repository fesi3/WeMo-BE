package com.wemo.backend.domain.meeting.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.meeting.dto.MeetingPlanListResponse;
import com.wemo.backend.domain.meeting.dto.MeetingReviewListResponse;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.user.dto.UserListInfo;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.wemo.backend.domain.attendance.entity.QAttendance.attendance;
import static com.wemo.backend.domain.image.entity.QImage.image;
import static com.wemo.backend.domain.meetingMember.entity.QMeetingMember.meetingMember;
import static com.wemo.backend.domain.plan.entity.QPlan.plan;
import static com.wemo.backend.domain.review.entity.QReview.review;
import static com.wemo.backend.domain.user.entity.QUser.user;

public class MeetingQueryDslImpl implements MeetingQueryDsl {

    private final JPAQueryFactory queryFactory;

    public MeetingQueryDslImpl(EntityManager em) {

        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<UserListInfo> getMemberListByMeeting(Meeting meeting, Pageable pageable) {

        JPAQuery<UserListInfo> queryBuilder = queryFactory
                .select(
                        Projections.constructor(
                                UserListInfo.class,
                                user.nickname,
                                user.profileImagePath,
                                user.createdAt
                        )
                )
                .from(meetingMember)
                .join(meetingMember.user, user)
                .where(meetingMember.meeting.eq(meeting));

        // 페이징 처리된 회원 목록을 가져옴
        List<UserListInfo> userListInfos = queryBuilder
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // 전체 멤버 수를 구하는 쿼리 (count)
        long total = Optional.ofNullable(queryFactory
                .select(meetingMember.count())
                .from(meetingMember)
                .where(meetingMember.meeting.eq(meeting))
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(userListInfos, pageable, total);
    }

    @Override
    public Page<MeetingPlanListResponse> getPlanListByMeeting(Meeting meeting, Pageable pageable) {

        JPAQuery<MeetingPlanListResponse> queryBuilder = queryFactory
                .select(
                        Projections.constructor(
                                MeetingPlanListResponse.class,
                                plan.id,
                                plan.planName,
                                plan.dateTime,
                                Expressions.as(
                                        queryFactory.select(attendance.count())
                                                .from(attendance)
                                                .where(attendance.plan.id.eq(plan.id)
                                                        .and(attendance.deletedAt.isNull())),
                                        "participants"
                                ),
                                plan.capacity,
                                Expressions.as(
                                        queryFactory.select(image.fileUrl)
                                                .from(image)
                                                .where(image.entityId.eq(plan.id),
                                                        image.entityType.eq(Image.EntityType.PLAN),
                                                        image.main.eq(true)),
                                        "planImagePath"
                                ),
                                plan.opened,
                                plan.fulled
                        )
                )
                .from(plan)
                .leftJoin(plan.user, user)
                .leftJoin(attendance).on(attendance.plan.eq(plan))
                .where(plan.meeting.eq(meeting))
                .orderBy(plan.dateTime.desc())
                .groupBy(plan.id);

        List<MeetingPlanListResponse> planListResponses = queryBuilder
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // 전체 멤버 수를 구하는 쿼리 (count)
        long total = Optional.ofNullable(queryFactory
                .select(plan.count())
                .from(plan)
                .leftJoin(plan.user, user)
                .leftJoin(attendance).on(attendance.plan.eq(plan))
                .where(plan.meeting.eq(meeting))
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(planListResponses, pageable, total);
    }

    @Override
    public Page<MeetingReviewListResponse> getReviewListByMeeting(Meeting meeting, Pageable pageable) {

        JPAQuery<MeetingReviewListResponse> queryBuilder = queryFactory
                .select(
                        Projections.constructor(
                                MeetingReviewListResponse.class,
                                user.nickname,
                                user.profileImagePath,
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
                .leftJoin(review.user, user)
                .where(review.plan.meeting.eq(meeting))
                .orderBy(review.createdAt.desc())
                .groupBy(review.id);

        List<MeetingReviewListResponse> reviewListResponses = queryBuilder
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        long total = Optional.ofNullable(queryFactory
                .select(review.count())
                .from(review)
                .leftJoin(review.user, user)
                .where(review.plan.meeting.eq(meeting))
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(reviewListResponses, pageable, total);
    }

}
