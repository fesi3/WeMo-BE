package com.wemo.backend.domain.review.repository;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserAndPlan(User user, Plan plan);

}
