package com.wemo.backend.domain.lightningJoin.service;

import com.wemo.backend.domain.lightning.entity.Lightning;

public interface LightningJoinReader {

    int getParticipantsCount(Lightning lightning);

}
