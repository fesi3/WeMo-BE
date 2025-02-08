package com.wemo.backend.domain.lightning.service;

import com.wemo.backend.domain.lightning.dto.LightningRequest;
import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightning.entity.LightningType;
import com.wemo.backend.domain.region.entity.District;
import com.wemo.backend.domain.user.entity.User;

public interface LightningStore {

    Lightning store(User user, LightningType lightningType, District district, LightningRequest request);

}
