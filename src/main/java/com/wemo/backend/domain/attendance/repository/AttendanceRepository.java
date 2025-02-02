package com.wemo.backend.domain.attendance.repository;

import com.wemo.backend.domain.attendance.entity.Attendance;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUserAndPlan(User user, Plan plan);

    boolean existsByUserAndPlan(User user, Plan plan);

    List<Attendance> findAllByPlan(Plan plan);

    long countByPlan(Plan plan);

    List<Attendance> findAllByUserAndPlanOpened(User user, boolean opened);

    void deleteByUser(User user);

}
