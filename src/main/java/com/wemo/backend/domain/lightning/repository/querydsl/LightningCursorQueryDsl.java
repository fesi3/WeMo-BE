package com.wemo.backend.domain.lightning.repository.querydsl;

import com.wemo.backend.domain.lightning.dto.LightningCursorPagingResponse;

public interface LightningCursorQueryDsl {

    LightningCursorPagingResponse getLightningMeetingList(String email, Long cursor, int size, String province, String district, Long lightningTypeId, Integer lightningTimeId, Double latitude, Double longitude, Double radius);

}
