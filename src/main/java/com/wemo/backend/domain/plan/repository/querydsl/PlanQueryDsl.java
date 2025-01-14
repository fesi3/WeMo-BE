package com.wemo.backend.domain.plan.repository.querydsl;

import com.wemo.backend.domain.plan.dto.PlanListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlanQueryDsl {

    Page<PlanListResponse> getLikedPlanList(String email, Pageable pageable, String query, String province, String district, String startDate, String endDate, Long categoryId, String sort);

}
