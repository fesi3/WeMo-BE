package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.entity.Attendance;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;

import java.util.List;

public interface PlanReader {

    Plan getPlan(Long planId);

    Attendance validateAttendance(User user, Plan plan);

    List<Plan> getPlanByMeeting(Meeting meeting);

    List<Attendance> getAttendanceList(Plan plan);

}
