package com.wemo.backend.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserPlanListResponseForCalendar {

    private Long planId;

    private String planName;

    private String planImagePath;

    private LocalDateTime dateTime;

    private String category;

    private String addressDetail;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isOpened;

    private boolean isCompleted;

    @JsonProperty("isOpened")
    public boolean getIsOpened() {

        return isOpened;
    }

    @JsonProperty("isCompleted")
    public boolean getIsCompleted() {

        return isCompleted;
    }

}
