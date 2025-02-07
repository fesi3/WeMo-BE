package com.wemo.backend.domain.lightning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LightningCursorPagingResponse {

    private int lightningCount;

    private List<LightningListResponse> lightningList;

    private Long nextCursor;

    public LightningCursorPagingResponse(List<LightningListResponse> lightningList, int lightningCount) {

        this.lightningCount = lightningCount;
        this.lightningList = lightningList;
        this.nextCursor = lightningList.isEmpty() ? null : lightningList.get(lightningList.size() - 1).getLightningId();
    }

}
