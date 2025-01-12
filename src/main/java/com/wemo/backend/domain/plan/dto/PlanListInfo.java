package com.wemo.backend.domain.plan.dto;

import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.plan.entity.Plan;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PlanListInfo {

    private Long planId;

    private String planName;

    private LocalDateTime dateTime;

    private int participants;

    private int capacity;

    private String planImagePath;

    private boolean isOpened;

    private boolean isFulled;

    public static PlanListInfo fromEntity(Plan plan, int participants, Image image) {

        return PlanListInfo.builder()
                .planId(plan.getId())
                .planName(plan.getPlanName())
                .dateTime(plan.getDateTime())
                .participants(participants)
                .capacity(plan.getCapacity())
                .planImagePath(image.getFileUrl())
                .isOpened(plan.isOpened())
                .isFulled(plan.isFulled())
                .build();
    }

}
