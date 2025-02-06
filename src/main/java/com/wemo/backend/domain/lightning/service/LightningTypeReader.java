package com.wemo.backend.domain.lightning.service;

import com.wemo.backend.domain.lightning.entity.LightningType;

public interface LightningTypeReader {

    LightningType getLightningType(Long lightningTypeId);

}
