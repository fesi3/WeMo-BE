package com.wemo.backend.domain.plan.repository.querydsl;

import com.wemo.backend.domain.plan.dto.PlanCursorPagingResponse;

public interface PlanCursorQueryDsl {

    PlanCursorPagingResponse getPlanListByUser(String email, Long cursor, int size, String query, String province, String district, String startDate, String endDate, Long categoryId, String sort);

    PlanCursorPagingResponse getPlanListByGuest(Long cursor, int size, String query, String province, String district, String startDate, String endDate, Long categoryId, String sort);

}
