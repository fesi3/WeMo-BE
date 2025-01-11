package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;

public interface PlanReader {

    Plan getPlan(Long planId);

    void validateAttendance(User user, Plan plan);

}
