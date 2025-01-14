package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.attendance.entity.Attendance;
import com.wemo.backend.domain.attendance.service.AttendanceReader;
import com.wemo.backend.domain.attendance.service.AttendanceStore;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.dto.PlanCreateRequest;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.attendance.repository.AttendanceRepository;
import com.wemo.backend.domain.plan.repository.PlanRepository;
import com.wemo.backend.domain.region.entity.District;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.wemo.backend.global.exception.ErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanStoreImpl implements PlanStore{

    private final PlanRepository planRepository;

    private final AttendanceReader attendanceReader;

    private final AttendanceStore attendanceStore;

    @Override
    @Transactional
    public Plan storePlan(PlanCreateRequest request, District district, User user, Meeting meeting) {

        Plan plan = Plan.builder()
                .planName(request.getPlanName())
                .content(request.getContent())
                .dateTime(LocalDateTime.parse(request.getDateTime()))
                .registrationEnd(LocalDateTime.parse(request.getRegistrationEnd()))
                .capacity(request.getCapacity())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .address(request.getAddress())
                .district(district)
                .opened(false)
                .canceled(false)
                .fulled(false)
                .user(user)
                .meeting(meeting)
                .build();

        planRepository.save(plan);

        return plan;
    }

}
