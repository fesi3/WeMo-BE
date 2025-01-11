package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.plan.dto.PlanCreateRequest;
import com.wemo.backend.domain.plan.dto.PlanCreateResponse;
import org.springframework.stereotype.Service;

@Service
public interface PlanService {

    PlanCreateResponse createPlan(String email, PlanCreateRequest request, Long meetingId);

}
