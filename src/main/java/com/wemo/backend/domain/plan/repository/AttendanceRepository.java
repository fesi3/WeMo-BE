package com.wemo.backend.domain.plan.repository;

import com.wemo.backend.domain.plan.entity.Attendance;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByUserAndPlan(User user, Plan plan);

    List<Attendance> findAllByPlan(Plan plan);
}
