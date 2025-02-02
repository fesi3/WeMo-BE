package com.wemo.backend.domain.attendance.service;

import com.wemo.backend.domain.attendance.entity.Attendance;
import com.wemo.backend.domain.attendance.repository.AttendanceRepository;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.wemo.backend.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class AttendanceStoreImpl implements AttendanceStore {

    private final AttendanceRepository attendanceRepository;

    private final AttendanceReader attendanceReader;

    // 참여 내역 저장
    @Override
    @Transactional
    public void storeAttendance(User user, Plan plan) {

        Attendance attendance = Attendance.builder()
                .user(user)
                .plan(plan)
                .build();

        attendanceRepository.save(attendance);
    }

    // 일정 참여
    @Override
    @Transactional
    public void attendPlan(User user, Plan plan) {

        // 이미 참여한 일정 예외 처리
        boolean alreadyJoined = attendanceReader.existAttendance(user, plan);
        if (alreadyJoined) throw new CustomException(ALREADY_JOINED_PLAN);

        // 모집 정원 마감 예외 처리
        int currentParticipants = (int) attendanceRepository.countByPlan(plan);

        if (currentParticipants >= plan.getCapacity()) throw new CustomException(PLAN_IS_FULL);

        // 참여 정보 저장
        storeAttendance(user, plan);

        // 인원 수 변경에 따른 일정 상태값 변경
        updatePlanStatus(plan, currentParticipants + 1); // 기존 참여 인원에 새 참여자를 추가
    }

    // 일정 참여 취소
    @Override
    @Transactional
    public void quitPlan(User user, Plan plan) {

        // 주최자 필수 참여 예외 처리
        if (plan.getUser().getEmail().equals(user.getEmail())) throw new CustomException(HOST_ATTENDANCE_REQUIRED);

        // 이전 참여 내역 조회 후 존재하면 삭제
        Attendance attendance = attendanceReader.validateAttendance(user, plan);
        attendanceRepository.delete(attendance);

        // 인원 수 변경에 따른 일정 상태값 변경
        int currentParticipants = (int) attendanceRepository.countByPlan(plan);
        updatePlanStatus(plan, currentParticipants - 1);

    }

    @Transactional
    private void updatePlanStatus(Plan plan, int participants) {
        // 최소 인원 충족 시 개설 확정
        if (participants >= 5) {
            plan.open();
        } else {
            plan.close();
        }

        // 정원 마감 시 개설 마감
        if (participants >= plan.getCapacity()) {
            plan.full();
        } else {
            plan.hasCapacity();
        }

    }

}
