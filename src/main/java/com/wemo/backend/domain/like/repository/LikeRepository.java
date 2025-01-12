package com.wemo.backend.domain.like.repository;

import com.wemo.backend.domain.like.entity.Like;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserAndPlan(User user, Plan plan);

}
