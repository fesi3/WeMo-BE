package com.wemo.backend.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PlanCursorPagingResponse {

    private int planCount;

    private List<PlanListResponse> planList;

    private Long nextCursor;

    public PlanCursorPagingResponse(List<PlanListResponse> planListResponses, int planCount) {

        this.planCount = planCount;
        this.planList = planListResponses;
        this.nextCursor = planListResponses.isEmpty() ? null : planListResponses.get(planListResponses.size() - 1).getPlanId();


    }

}
