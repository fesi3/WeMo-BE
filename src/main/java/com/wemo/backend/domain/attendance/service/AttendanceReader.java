package com.wemo.backend.domain.attendance.service;

import com.wemo.backend.domain.attendance.entity.Attendance;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;

import java.util.List;

public interface AttendanceReader {

    Attendance validateAttendance(User user, Plan plan);

    List<Attendance> getAttendanceList(Plan plan);

    void delete(Attendance attendance);

    boolean existAttendance(User user, Plan plan);

    int getParticipantsCount(Plan plan);

    List<Attendance> getAttendanceByUser(User user);

}
