package com.wemo.backend.domain.review.repository;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.review.repository.querydsl.ReviewQueryDsl;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewQueryDsl {

    boolean existsByUserAndPlan(User user, Plan plan);

    List<Review> findAllByPlan(Plan plan);

    List<Review> findAllByUser(User user);

    void deleteAllByPlan(Plan plan);

}
