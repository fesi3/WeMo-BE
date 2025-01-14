package com.wemo.backend.domain.like.service;

import com.wemo.backend.domain.like.entity.Likes;
import com.wemo.backend.domain.like.repository.LikeRepository;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeStoreImpl implements LikeStore {

    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public void storeLike(User user, Plan plan) {

        Likes like = Likes.builder()
                .user(user)
                .plan(plan)
                .build();

        likeRepository.save(like);
    }

    @Override
    public void deleteLike(User user, Plan plan) {

        Likes like = likeRepository.findByUserAndPlan(user, plan);

        likeRepository.delete(like);
    }

}
