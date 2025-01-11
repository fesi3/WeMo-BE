package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.repository.AttendanceRepository;
import com.wemo.backend.domain.plan.repository.PlanRepository;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.wemo.backend.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class PlanReaderImpl implements PlanReader {

    private final PlanRepository planRepository;

    private final AttendanceRepository attendanceRepository;

    @Override
    public Plan getPlan(Long planId) {

        return planRepository.findById(planId).orElseThrow(
                () -> new CustomException(ILLEGAL_PLAN_NOT_FOUND)
        );
    }

    @Override
    public void validateAttendance(User user, Plan plan) {

        boolean exists = attendanceRepository.existsByUserAndPlan(user, plan);

        if (!exists) throw new CustomException(PLAN_ATTENDANCE_NOT_FOUND);

    }

}
