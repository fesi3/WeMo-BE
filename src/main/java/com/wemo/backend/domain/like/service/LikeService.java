package com.wemo.backend.domain.like.service;

import org.springframework.stereotype.Service;

@Service
public interface LikeService {

    String likePlan(String email, Long planId);

    String deleteLikePlan(String email, Long planId);

}
