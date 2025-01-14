package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.entity.Plan;

import java.util.List;

public interface PlanReader {

    Plan getPlan(Long planId);

    List<Plan> getPlanByMeeting(Meeting meeting);

    void delete(Plan plan);

}
