package com.wemo.backend.domain.meeting.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.meeting.dto.*;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.user.dto.UserListInfo;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

import static com.wemo.backend.domain.attendance.entity.QAttendance.attendance;
import static com.wemo.backend.domain.image.entity.QImage.image;
import static com.wemo.backend.domain.meeting.entity.QMeeting.meeting;
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

    @Override
    public MeetingCursorPagingResponse getMeetingList(Long cursor, int size, Long categoryId, String sort) {

        BooleanBuilder listConditions = new BooleanBuilder();
        if (cursor != null) {
            listConditions.and(meeting.id.lt(cursor));
        }

        List<MeetingListResponse> meetingListResponses = queryFactory
                .select(
                        Projections.constructor(
                                MeetingListResponse.class,
                                user.email,
                                meeting.id,
                                meeting.meetingName,
                                meeting.description,
                                Expressions.as(
                                        queryFactory.select(image.fileUrl)
                                                .from(image)
                                                .where(image.entityId.eq(meeting.id),
                                                        image.entityType.eq(Image.EntityType.MEETING),
                                                        image.main.eq(true)),
                                        "meetingImagePath"
                                ),
                                Expressions.as(
                                        JPAExpressions.select(meetingMember.count())
                                                .from(meetingMember)
                                                .where(meetingMember.meeting.id.eq(meeting.id)
                                                        .and(meetingMember.deletedAt.isNull())),
                                        "memberCount"
                                ),
                                meeting.category.categoryName
                        )
                )
                .from(meeting)
                .leftJoin(meeting.user, user)
                .where(listConditions)
                .where(buildFilterConditions(categoryId))
                .where(meeting.deletedAt.isNull())
                .orderBy(
                        getOrderSpecifier(sort),  // memberCount 정렬 기준
                        meeting.createdAt.desc()  // createdAt 정렬 기준
                )
                .limit(size)
                .fetch();  // .stream().toList()를 .fetch()로 수정

        long meetingCount = Optional.ofNullable(
                queryFactory
                        .select(meeting.count())
                        .from(meeting)
                        .where(buildFilterConditions(categoryId))
                        .where(meeting.deletedAt.isNull())
                        .fetchOne()
        ).orElse(0L);

        return new MeetingCursorPagingResponse(meetingListResponses, (int) meetingCount);
    }

    @Override
    public MeetingCursorPagingResponse getMeetingListV2(Long cursor, int size, Long categoryId, String sort) {

        BooleanBuilder listConditions = new BooleanBuilder();
        if (cursor != null) {
            listConditions.and(meeting.id.lt(cursor));
        }

        // 1️⃣ Meeting 리스트 조회 (planList 제외)
        List<MeetingListResponseV2> meetingListResponses = queryFactory
                .select(Projections.constructor(
                        MeetingListResponseV2.class,
                        user.email,
                        meeting.id,
                        meeting.meetingName,
                        meeting.description,
                        Expressions.as(
                                queryFactory.select(image.fileUrl)
                                        .from(image)
                                        .where(image.entityId.eq(meeting.id),
                                                image.entityType.eq(Image.EntityType.MEETING),
                                                image.main.eq(true)),
                                "meetingImagePath"
                        ),
                        Expressions.as(
                                JPAExpressions.select(meetingMember.count())
                                        .from(meetingMember)
                                        .where(meetingMember.meeting.id.eq(meeting.id)
                                                .and(meetingMember.deletedAt.isNull())),
                                "memberCount"
                        ),
                        meeting.category.categoryName,
                        Expressions.as(
                                JPAExpressions.select(plan.count())
                                        .from(plan)
                                        .where(plan.meeting.eq(meeting)
                                                .and(plan.canceled.eq(false))
                                                .and(plan.deletedAt.isNull())),
                                "planCount"
                        )
                ))
                .from(meeting)
                .leftJoin(meeting.user, user)
                .where(listConditions)
                .where(buildFilterConditions(categoryId))
                .where(meeting.deletedAt.isNull())
                .orderBy(
                        getOrderSpecifier(sort), // memberCount 정렬 기준
                        meeting.createdAt.desc()  // createdAt 정렬 기준
                )
                .limit(size)
                .fetch();

        // 2️⃣ 각 Meeting ID에 대해 최근 3개의 Plan 리스트 조회
        List<Long> meetingIds = meetingListResponses.stream()
                .map(MeetingListResponseV2::getMeetingId)
                .toList();

        Map<Long, List<MeetingListPlanListResponse>> planMap = queryFactory
                .select(Projections.constructor(
                        MeetingListPlanListResponse.class,
                        plan.id,
                        plan.meeting.id,
                        plan.dateTime,
                        plan.fulled
                ))
                .from(plan)
                .where(plan.meeting.id.in(meetingIds))
                .orderBy(plan.dateTime.desc()) // 최신 일정 순으로 정렬
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        MeetingListPlanListResponse::getMeetingId,
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().limit(4).toList())
                ));
        // 3️⃣ meetingListResponses에 planList 추가
        for (MeetingListResponseV2 response : meetingListResponses) {
            response.setPlanList(planMap.getOrDefault(response.getMeetingId(), new ArrayList<>()));
        }

        long meetingCount = Optional.ofNullable(
                queryFactory
                        .select(meeting.count())
                        .from(meeting)
                        .where(buildFilterConditions(categoryId))
                        .where(meeting.deletedAt.isNull())
                        .fetchOne()
        ).orElse(0L);

        return new MeetingCursorPagingResponse(meetingListResponses, (int) meetingCount);
    }

    private BooleanExpression buildFilterConditions(Long categoryId) {

        BooleanExpression condition = meeting.deletedAt.isNull();

        if (categoryId != null) {
            if (categoryId == 1) {
                condition = condition.and(meeting.category.parentId.eq(categoryId));
            } else {
                condition = condition.and(meeting.category.id.eq(categoryId));
            }
        }

        return condition;
    }

    // 정렬 조건 적용 함수
    private OrderSpecifier<?> getOrderSpecifier(String sort) {

        if ("memberDesc".equals(sort)) {
            return Expressions.numberTemplate(Long.class,
                    "(select count(*) from MeetingMember mm where mm.meeting.id = {0} and mm.deletedAt is null)",
                    meeting.id).desc();
        }
        return meeting.createdAt.desc(); // 기본 최신순 정렬
    }

}
