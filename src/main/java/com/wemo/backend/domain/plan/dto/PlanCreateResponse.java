package com.wemo.backend.domain.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.entity.Plan;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PlanCreateResponse {

    private Long planId;

    private String planName;

    private String category;

    private String address;

    private String addressDetail;

    private double longitude;

    private double latitude;

    private List<String> planImagePath;

    private String content;

    private LocalDateTime dateTime;

    private LocalDateTime registrationEnd;

    private int capacity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isOpened;

    private boolean isFulled;

    @JsonProperty("isOpened")
    public boolean getIsOpened() {

        return isOpened;
    }

    @JsonProperty("isFulled")
    public boolean getIsFulled() {

        return isFulled;
    }

    // 이미지가 있는 경우
    public static PlanCreateResponse fromEntityWithImage(Plan plan, Meeting meeting, List<String> planImagePath) {

        return PlanCreateResponse.builder()
                .planId(plan.getId())
                .planName(plan.getPlanName())
                .category(meeting.getCategory().getCategoryName())
                .address(plan.getAddress())
                .addressDetail(plan.getAddressDetail())
                .longitude(plan.getLongitude())
                .latitude(plan.getLatitude())
                .planImagePath(planImagePath)
                .content(plan.getContent())
                .dateTime(plan.getDateTime())
                .registrationEnd(plan.getRegistrationEnd())
                .capacity(plan.getCapacity())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .isOpened(plan.isOpened())
                .isFulled(plan.isFulled())
                .build();
    }

    // 이미지가 없는 경우
    public static PlanCreateResponse fromEntity(Plan plan, Meeting meeting) {

        return PlanCreateResponse.builder()
                .planId(plan.getId())
                .planName(plan.getPlanName())
                .category(meeting.getCategory().getCategoryName())
                .address(plan.getAddress())
                .addressDetail(plan.getAddressDetail())
                .longitude(plan.getLongitude())
                .latitude(plan.getLatitude())
                .content(plan.getContent())
                .dateTime(plan.getDateTime())
                .registrationEnd(plan.getRegistrationEnd())
                .capacity(plan.getCapacity())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .isOpened(plan.isOpened())
                .isFulled(plan.isFulled())
                .build();
    }

}
