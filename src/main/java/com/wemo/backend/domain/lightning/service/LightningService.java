package com.wemo.backend.domain.lightning.service;

import com.wemo.backend.domain.lightning.dto.LightningCreateRequest;
import com.wemo.backend.domain.lightning.dto.LightningCreateResponse;
import org.springframework.stereotype.Service;

@Service
public interface LightningService {

    LightningCreateResponse createLightnings(String email, LightningCreateRequest request);

}
