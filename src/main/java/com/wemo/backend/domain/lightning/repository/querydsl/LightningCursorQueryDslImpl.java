package com.wemo.backend.domain.lightning.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wemo.backend.domain.lightning.dto.LightningCursorPagingResponse;
import com.wemo.backend.domain.lightning.dto.LightningListResponse;
import com.wemo.backend.domain.lightning.entity.DateType;
import com.wemo.backend.domain.region.entity.QDistrict;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.wemo.backend.domain.lightning.entity.QLightning.lightning;
import static com.wemo.backend.domain.lightningJoin.entity.QLightningJoin.lightningJoin;
import static com.wemo.backend.domain.user.entity.QUser.user;

@Slf4j
public class LightningCursorQueryDslImpl implements LightningCursorQueryDsl {

    private final JPAQueryFactory queryFactory;

    public LightningCursorQueryDslImpl(EntityManager em) {

        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public LightningCursorPagingResponse getLightningMeetingList(String email, Long cursor, int size, String province, String district, Long lightningTypeId, Integer lightningTimeId, Double latitude, Double longitude, Double radius) {

        log.info("{} 번개 모임 목록 조회 요청", (email != null) ? email : "비회원");

        // 커서 조건을 제외한 기본 조건 빌더
        BooleanBuilder baseConditions = new BooleanBuilder()
                .and(buildFilterConditions(province, district, lightningTypeId, lightningTimeId, latitude, longitude, radius));

        // 목록 조회 쿼리 (커서 조건 포함)
        BooleanBuilder listConditions = new BooleanBuilder(baseConditions);
        if (cursor != null) {
            listConditions.and(lightning.id.lt(cursor));
        }

        List<LightningListResponse> lightningListResponses = queryFactory
                .select(
                        Projections.constructor(
                                LightningListResponse.class,
                                lightning.id,
                                lightning.lightningName,
                                lightning.lightningType.lightTypeName,
                                lightning.dateType.stringValue(),
                                lightning.lightningDate,
                                lightning.lightningCapacity,
                                Expressions.as(
                                        JPAExpressions.select(lightningJoin.count())
                                                .from(lightningJoin)
                                                .where(lightningJoin.lightning.id.eq(lightning.id)),
                                        "lightningParticipants"
                                ),
                                lightning.address,
                                lightning.latitude,
                                lightning.longitude,
                                lightning.createdAt,
                                lightning.updatedAt,
                                user.nickname,
                                user.profileImagePath,
                                Expressions.constant(false)  // 기본값 추가
                        )
                )
                .from(lightning)
                .leftJoin(lightning.user, user)
                .where(listConditions)
                .limit(size)
                .orderBy(lightning.createdAt.desc())
                .fetch();

        // 쿼리 결과 후, lightningTime 값에 대해 getDisplayName()을 호출하여 변환
        lightningListResponses.forEach(response -> {
            response.setLightningTime(Enum.valueOf(DateType.class, response.getLightningTime()).getDisplayName());

            // 이메일이 null이면 참여 여부 false, 그렇지 않으면 DB에서 조회
            boolean isJoined = (email != null) &&
                    queryFactory
                            .selectOne()
                            .from(lightningJoin)
                            .where(lightningJoin.lightning.id.eq(response.getLightningId())
                                    .and(lightningJoin.user.email.eq(email)))
                            .fetchFirst() != null;

            response.setIsJoined(isJoined);
        });

        return new LightningCursorPagingResponse(lightningListResponses, lightningListResponses.size());
    }

    private BooleanExpression buildFilterConditions(String province, String district, Long lightningTypeId, Integer lightningTimeId, Double latitude, Double longitude, Double radius) {

        BooleanExpression condition = lightning.deletedAt.isNull(); // 기본 필터 조건

        condition = buildRegionFilter(province, district, condition, lightning.district);

        if (lightningTypeId != null) {
            condition = condition.and(lightning.lightningType.lightningTypeId.eq(lightningTypeId));
        }

        if (lightningTimeId != null && (lightningTimeId == 1 || lightningTimeId == 2 || lightningTimeId == 3)) {

            DateType dateType = DateType.fromId(lightningTimeId);
            condition = condition.and(lightning.dateType.eq(dateType));
        }

        return buildAddressFilter(province, district, latitude, longitude, radius, condition);
    }

    public static BooleanExpression buildRegionFilter(String province, String district, BooleanExpression condition, QDistrict district2) {

        if (province != null && !province.isEmpty()) {
            condition = condition.and(district2.province.provinceName.eq(province));
        }

        if (district != null && !district.isEmpty()) {
            condition = condition.and(district2.districtName.eq(district));
        }
        return condition;
    }


    public static BooleanExpression buildAddressFilter(String province, String district, Double latitude, Double longitude, Double radius, BooleanExpression condition) {

        if ((province == null || province.isEmpty()) && (district == null || district.isEmpty())) {
            if (latitude != null && longitude != null && radius != null) {
                double radiusInMeters = radius * 1000; // km → m 변환

                NumberExpression<Double> distance = Expressions.numberTemplate(Double.class,
                        "ST_Distance_Sphere(point({0}, {1}), point(lightning.longitude, lightning.latitude))",
                        longitude, latitude);

                condition = condition.and(distance.loe(radiusInMeters));
            }
        }

        return condition;
    }

}
