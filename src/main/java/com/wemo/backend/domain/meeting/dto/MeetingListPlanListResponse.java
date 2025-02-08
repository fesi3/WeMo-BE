package com.wemo.backend.domain.meeting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MeetingListPlanListResponse {

    private Long planId;

    private Long meetingId;

    private LocalDateTime dateTime;

    private boolean isFulled;

    @JsonProperty("isFulled")
    public boolean getIsFulled() {

        return isFulled;
    }

}
