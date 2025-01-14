package com.wemo.backend.domain.attendance.service;

import com.wemo.backend.domain.attendance.entity.Attendance;
import com.wemo.backend.domain.attendance.repository.AttendanceRepository;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wemo.backend.global.exception.ErrorCode.PLAN_ATTENDANCE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class AttendanceReaderImpl implements AttendanceReader {

    private final AttendanceRepository attendanceRepository;

    @Override
    public Attendance validateAttendance(User user, Plan plan) {

        return attendanceRepository.findByUserAndPlan(user, plan).orElseThrow(
                () -> new CustomException(PLAN_ATTENDANCE_NOT_FOUND)
        );
    }

    @Override
    public List<Attendance> getAttendanceList(Plan plan) {

        return attendanceRepository.findAllByPlan(plan);
    }

    @Override
    public void delete(Attendance attendance) {

        attendanceRepository.delete(attendance);
    }

    /**
     * 일정 참여 내역 조회
     *
     * @param user 유저 객체
     * @param plan 일정 객체
     * @return 참여 내역이 있다면 true, 없다면 false
     */
    @Override
    public boolean existAttendance(User user, Plan plan) {

        return attendanceRepository.existsByUserAndPlan(user, plan);

    }

    @Override
    public int getParticipantsCount(Plan plan) {

        return (int) attendanceRepository.countByPlan(plan);
    }

    @Override
    public List<Attendance> getAttendanceByUser(User user) {

        return attendanceRepository.findAllByUserAndPlanOpened(user, true);
    }

}
