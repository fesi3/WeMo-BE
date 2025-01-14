package com.wemo.backend.domain.meeting.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.user.dto.UserListInfo;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.wemo.backend.domain.meetingMember.entity.QMeetingMember.meetingMember;
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

}
