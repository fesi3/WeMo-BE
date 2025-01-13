package com.wemo.backend.domain.user.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wemo.backend.domain.user.dto.UserMeetingListResponse;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.wemo.backend.domain.meeting.entity.QMeeting.meeting;
import static com.wemo.backend.domain.meeting.entity.QMeetingMember.meetingMember;
import static com.wemo.backend.domain.user.entity.QUser.user;

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
                                meeting.user.email, // meeting.user.email 참조
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
                .groupBy(meeting.id) // 그룹화
                .orderBy(meeting.createdAt.desc());

        // 페이징 처리
        List<UserMeetingListResponse> userMeetingListResponses = queryBuilder
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // 총 갯수 조회
        long total = Optional.ofNullable(
                queryFactory
                .select(meeting.count())
                .from(meeting)
                .leftJoin(meetingMember).on(meetingMember.meeting.eq(meeting))
                .where(meeting.user.email.eq(email)
                    .or(meetingMember.user.email.eq(email)))
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(userMeetingListResponses, pageable, total);
    }

}
