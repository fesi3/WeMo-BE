package com.wemo.backend.domain.plan.repository;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.repository.querydsl.PlanCursorQueryDsl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long>, PlanCursorQueryDsl {

    List<Plan> findAllByMeeting(Meeting meeting);
}
