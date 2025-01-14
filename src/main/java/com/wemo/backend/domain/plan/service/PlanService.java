package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.plan.dto.PlanCreateRequest;
import com.wemo.backend.domain.plan.dto.PlanCreateResponse;
import com.wemo.backend.domain.plan.dto.PlanCursorPagingResponse;
import com.wemo.backend.domain.plan.dto.PlanDetailResponse;
import org.springframework.stereotype.Service;

@Service
public interface PlanService {

    PlanCreateResponse createPlan(String email, PlanCreateRequest request, Long meetingId);

    String joinPlan(String email, Long planId);

    PlanCursorPagingResponse getPlanList(UserDetailsImpl userDetails, Long cursor, int size, String query, String province, String district, String startDate, String endDate, Long categoryId, String sort);

    PlanDetailResponse getPlanDetail(UserDetailsImpl userDetails, Long planId);

    String cancelPlan(String email, Long planId);

}
