package com.wemo.backend.domain.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.plan.entity.Plan;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PlanListInfo {

    private Long planId;

    private String planName;

    private LocalDateTime dateTime;

    private int participants;

    private int capacity;

    private List<String> planImagePath;

    @JsonProperty("isOpened")
    private boolean isOpened;

    @JsonProperty("isFulled")
    private boolean isFulled;

    @JsonProperty("isOpened")
    public boolean getIsOpened() {
        return isOpened;
    }

    @JsonProperty("isFulled")
    public boolean getIsFulled() {
        return isFulled;
    }
    public static PlanListInfo fromEntity(Plan plan, int participants, List<String> planImagePath) {

        return PlanListInfo.builder()
                .planId(plan.getId())
                .planName(plan.getPlanName())
                .dateTime(plan.getDateTime())
                .participants(participants)
                .capacity(plan.getCapacity())
                .planImagePath(planImagePath)
                .isOpened(plan.isOpened())
                .isFulled(plan.isFulled())
                .build();
    }

}
