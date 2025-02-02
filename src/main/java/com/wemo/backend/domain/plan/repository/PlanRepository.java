package com.wemo.backend.domain.plan.repository;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.repository.querydsl.PlanCursorQueryDsl;
import com.wemo.backend.domain.plan.repository.querydsl.PlanQueryDsl;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long>, PlanCursorQueryDsl, PlanQueryDsl {

    List<Plan> findAllByMeetingAndDeletedAtIsNull(Meeting meeting);

    List<Plan> findAllByUserAndDeletedAtIsNull(User user);

}
