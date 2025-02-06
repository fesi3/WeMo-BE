package com.wemo.backend.domain.lightningJoin.service;

import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.user.entity.User;

public interface LightningJoinStore {

    void store(User user, Lightning lightning);

}
