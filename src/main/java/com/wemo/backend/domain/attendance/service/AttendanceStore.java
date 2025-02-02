package com.wemo.backend.domain.attendance.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;

public interface AttendanceStore {

    void storeAttendance(User user, Plan plan);

    void attendPlan(User user, Plan plan);

    void quitPlan(User user, Plan plan);

    void deleteAllByUser(User user);

}
