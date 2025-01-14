package com.wemo.backend.domain.like.service;

import com.wemo.backend.domain.like.entity.Likes;
import com.wemo.backend.domain.like.repository.LikeRepository;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wemo.backend.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class LikeReaderImpl implements LikeReader{

    private final LikeRepository likeRepository;

    @Override
    public void validateLike(User user, Plan plan) {

        if (likeRepository.existsByUserAndPlan(user, plan)) throw new CustomException(ALREADY_LIKED_PLAN);

    }

    @Override
    public void validateLikeToDelete(User user, Plan plan) {

        if (!likeRepository.existsByUserAndPlan(user, plan)) throw new CustomException(LIKE_NOT_FOUND);
    }

    @Override
    public List<Likes> getLikeCountByUser(User user) {

        return likeRepository.findAllByUser(user);
    }

}
