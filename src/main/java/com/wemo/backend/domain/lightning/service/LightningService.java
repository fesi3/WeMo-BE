package com.wemo.backend.domain.lightning.service;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.lightning.dto.LightningCreateRequest;
import com.wemo.backend.domain.lightning.dto.LightningCreateResponse;
import com.wemo.backend.domain.lightning.dto.LightningCursorPagingResponse;
import com.wemo.backend.domain.lightning.dto.LightningDetailResponse;
import org.springframework.stereotype.Service;

@Service
public interface LightningService {

    LightningCreateResponse createLightnings(String email, LightningCreateRequest request);

    LightningCursorPagingResponse getLightningMeetingList(Long cursor, int size, String province, String district, Long lightningTypeId, Integer lightningTimeId, Double latitude, Double longitude, Double radius);

    LightningDetailResponse getLightningMeetingDetail(UserDetailsImpl userDetails, Long lightningId);

}
