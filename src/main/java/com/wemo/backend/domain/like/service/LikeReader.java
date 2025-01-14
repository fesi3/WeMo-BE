package com.wemo.backend.domain.like.service;

import com.wemo.backend.domain.like.entity.Likes;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;

import java.util.List;

public interface LikeReader {

    void validateLike(User user, Plan plan);

    void validateLikeToDelete(User user, Plan plan);

    List<Likes> getLikeCountByUser(User user);

}
