package com.wemo.backend.domain.plan.repository;

import com.wemo.backend.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
