package com.wemo.backend.domain.meeting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MeetingPlanListResponse {

    private Long planId;

    private String planName;

    private LocalDateTime dateTime;

    private Long participants;

    private int capacity;

    private String planImagePath;

    @JsonProperty("isOpened")
    private boolean isOpened;

    @JsonProperty("isFulled")
    private boolean isFulled;

}
