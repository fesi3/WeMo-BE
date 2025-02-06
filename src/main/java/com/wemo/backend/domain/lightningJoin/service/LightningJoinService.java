package com.wemo.backend.domain.lightningJoin.service;

import org.springframework.stereotype.Service;

@Service
public interface LightningJoinService {

    String participateLightningMeeting(String email, Long lightningId);

}
