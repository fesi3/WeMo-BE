package com.wemo.backend.domain.plan.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.plan.dto.PlanCursorPagingResponse;
import com.wemo.backend.domain.plan.dto.PlanListResponse;
import com.wemo.backend.domain.plan.entity.Plan;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.wemo.backend.domain.attendance.entity.QAttendance.attendance;
import static com.wemo.backend.domain.image.entity.QImage.image;
import static com.wemo.backend.domain.like.entity.QLikes.likes;
import static com.wemo.backend.domain.meeting.entity.QMeeting.meeting;
import static com.wemo.backend.domain.plan.entity.QPlan.plan;
import static com.wemo.backend.domain.region.entity.QDistrict.district;
import static com.wemo.backend.domain.region.entity.QProvince.province;
import static com.wemo.backend.domain.user.entity.QUser.user;

@Slf4j
public class PlanCursorQueryDslImpl implements PlanCursorQueryDsl {

    private static final String DEFAULT_SORT_FIELD = "createdAt";

    private final JPAQueryFactory queryFactory;

    public PlanCursorQueryDslImpl(EntityManager em) {

        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public PlanCursorPagingResponse getPlanListByUser(String email, Long cursor, int size, String query, String province, String district, String startDate, String endDate, Long categoryId, String sort, Double latitude, Double longitude, Double radius) {

        return getPlanList(email, cursor, size, query, province, district, startDate, endDate, categoryId, sort, latitude, longitude, radius);
    }

    @Override
    public PlanCursorPagingResponse getPlanListByGuest(Long cursor, int size, String query, String province, String district, String startDate, String endDate, Long categoryId, String sort, Double latitude, Double longitude, Double radius) {

        return getPlanList(null, cursor, size, query, province, district, startDate, endDate, categoryId, sort, latitude, longitude, radius);
    }

    private PlanCursorPagingResponse getPlanList(String email, Long cursor, int size, String query, String province, String district, String startDate, String endDate, Long categoryId, String sort, Double latitude, Double longitude, Double radius) {

        updateIsFulledStatus();

        log.info("{} 일정 목록 조회 요청", (email != null) ? email : "비회원");

        String sortField = (sort == null) ? DEFAULT_SORT_FIELD : sort;

        // 커서 조건을 제외한 기본 조건 빌더
        BooleanBuilder baseConditions = new BooleanBuilder()
                .and(buildFilterConditions(query, province, district, startDate, endDate, categoryId, latitude, longitude, radius));

        // 1. 목록 조회 쿼리 (커서 조건 포함)
        BooleanBuilder listConditions = new BooleanBuilder(baseConditions);
        if (cursor != null) {
            Plan targetPlan = queryFactory
                    .selectFrom(plan)
                    .where(plan.id.eq(cursor))
                    .fetchOne();

            if (targetPlan != null) {
                LocalDateTime cursorCloseDate = targetPlan.getRegistrationEnd();

                if (sortField.equals("closeDate")) {
                    listConditions.and(
                            plan.registrationEnd.gt(cursorCloseDate) // 현재 커서 이후의 마감일 데이터
                                    .or(
                                            plan.registrationEnd.eq(cursorCloseDate)
                                                    .and(plan.id.gt(cursor)) // 마감일자가 같으면 planId 기준 정렬
                                    )
                    );
                } else {
                    listConditions.and(plan.id.lt(cursor));
                }
            }
        }


        List<PlanListResponse> planListResponses = queryFactory
                .select(buildPlanListProjection(email))
                .from(plan)
                .leftJoin(plan.user, user)
                .where(listConditions)
                .where(plan.canceled.eq(false))
                .where(plan.meeting.deletedAt.isNull())
                .orderBy(
                        sortField.equals("closeDate")
                                ? plan.registrationEnd.asc().nullsLast()
                                : plan.createdAt.desc(),
                        plan.id.asc()) // 마감일자가 같으면 planId 기준 추가 정렬
                .limit(size)
                .fetch();

        // 2. 전체 개수 조회 쿼리 (커서 조건 제외)
        long planCount = Optional.ofNullable(
                queryFactory
                        .select(plan.count())
                        .from(plan)
                        .where(baseConditions)
                        .where(plan.meeting.deletedAt.isNull())
                        .fetchOne()
        ).orElse(0L);

        return new PlanCursorPagingResponse(planListResponses, (int) planCount);
    }

    public ConstructorExpression<PlanListResponse> buildPlanListProjection(String email) {

        return Projections.constructor(
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
                plan.latitude,
                plan.longitude,
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
                        "isLiked")
        );
    }

    private BooleanExpression buildFilterConditions(String query, String province, String district,
                                                    String startDate, String endDate, Long categoryId,
                                                    Double latitude, Double longitude, Double radius) {

        BooleanExpression condition = plan.canceled.eq(false);

        if (query != null && !query.isEmpty()) {
            condition = condition.and(plan.planName.contains(query));
        }

        if (province != null && !province.isEmpty()) {
            condition = condition.and(plan.district.province.provinceName.eq(province));
        }

        if (district != null && !district.isEmpty()) {
            condition = condition.and(plan.district.districtName.eq(district));
        }

        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            condition = condition.and(plan.dateTime.between(start.atStartOfDay(), end.atTime(23, 59, 59)));
        }

        if (categoryId != null) {
            if (categoryId == 1) {
                condition = condition.and(plan.meeting.category.parentId.eq(categoryId));
            } else {
                condition = condition.and(plan.meeting.category.id.eq(categoryId));
            }
        }

        // 위치 기반 필터링 추가 (필터가 없는 경우)
        if ((province == null || province.isEmpty()) && (district == null || district.isEmpty())) {
            if (latitude != null && longitude != null && radius != null) {
                double radiusInMeters = radius * 1000; // km → m 변환

                NumberExpression<Double> distance = Expressions.numberTemplate(Double.class,
                        "ST_Distance_Sphere(point({0}, {1}), point(plan.longitude, plan.latitude))",
                        longitude, latitude);

                condition = condition.and(distance.loe(radiusInMeters));
            }
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

}
