package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.plan.entity.Plan;

public interface PlanReader {

    Plan getPlan(Long planId);

}
