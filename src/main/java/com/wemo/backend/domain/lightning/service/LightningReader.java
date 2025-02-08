package com.wemo.backend.domain.lightning.service;


import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.user.entity.User;

public interface LightningReader {

    Lightning getLightningById(Long lightningId);

    Lightning validateLightningOwnership(User user, Long lightning);

}
