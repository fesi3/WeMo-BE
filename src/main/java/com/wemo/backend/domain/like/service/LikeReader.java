package com.wemo.backend.domain.like.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;

public interface LikeReader {

    void validateLike(User user, Plan plan);

}
