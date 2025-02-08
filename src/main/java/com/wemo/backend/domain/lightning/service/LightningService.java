package com.wemo.backend.domain.lightning.service;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.lightning.dto.LightningCursorPagingResponse;
import com.wemo.backend.domain.lightning.dto.LightningDetailResponse;
import com.wemo.backend.domain.lightning.dto.LightningRequest;
import com.wemo.backend.domain.lightning.dto.LightningResponse;
import org.springframework.stereotype.Service;

@Service
public interface LightningService {

    LightningResponse createLightnings(String email, LightningRequest request);

    LightningCursorPagingResponse getLightningMeetingList(Long cursor, int size, String province, String district, Long lightningTypeId, Integer lightningTimeId, Double latitude, Double longitude, Double radius);

    LightningDetailResponse getLightningMeetingDetail(UserDetailsImpl userDetails, Long lightningId);

    LightningResponse updateLightnings(String email, Long lightningId, LightningRequest lightningRequest);

}
