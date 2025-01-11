package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.dto.PlanCreateRequest;
import com.wemo.backend.domain.plan.entity.Attendance;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.repository.AttendanceRepository;
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

    private final AttendanceRepository attendanceRepository;

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

    @Override
    @Transactional
    public void joinPlan(User user, Plan plan) {

        // 이미 참여한 일정 예외 처리
        boolean alreadyJoined = attendanceRepository.existsByUserAndPlan(user, plan);
        if (alreadyJoined) throw new CustomException(ALREADY_JOINED_PLAN);

        // 모집 정원 마감 예외 처리
        List<Attendance> allByPlan = attendanceRepository.findAllByPlan(plan);
        if (allByPlan.size() >= plan.getCapacity()) throw new CustomException(PLAN_IS_FULLED);

        Attendance attendance = Attendance.builder()
                .user(user)
                .plan(plan)
                .build();

        attendanceRepository.save(attendance);

        // 인원 수 변경에 따른 일정 상태값 변경
        updatePlanStatus(plan, allByPlan.size() + 1); // 기존 참여 인원에 새 참여자를 추가
    }

    @Transactional
    private void updatePlanStatus(Plan plan, int participants) {
        // 최소 인원 충족 시 개설 확정
        if (participants >= 5) plan.open();

        // 정원 마감 시 개설 마감
        if (participants >= plan.getCapacity()) plan.close();

    }

}
