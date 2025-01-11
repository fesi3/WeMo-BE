package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.dto.PlanCreateRequest;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.region.entity.District;
import com.wemo.backend.domain.user.entity.User;

public interface PlanStore {

    Plan storePlan(PlanCreateRequest request, District district, User user, Meeting meeting);

}
