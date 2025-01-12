package com.wemo.backend.domain.like.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;

public interface LikeStore {

    void storeLike(User user, Plan plan);

    void deleteLike(User user, Plan plan);

}
