package com.wemo.backend.domain.like.repository;

import com.wemo.backend.domain.like.entity.Likes;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    boolean existsByUserAndPlan(User user, Plan plan);

    Likes findByUserAndPlan(User user, Plan plan);

    int countByPlan(Plan plan);

}
