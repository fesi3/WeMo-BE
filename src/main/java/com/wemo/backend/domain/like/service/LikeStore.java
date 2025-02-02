package com.wemo.backend.domain.like.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;

public interface LikeStore {

    void addLike(User user, Plan plan);

    void removeLike(User user, Plan plan);

    void deleteAllByUser(User user);

}
