package com.wemo.backend.domain.meeting.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MeetingUpdateRequest {

    @Size(min = 8, max = 500, message = "모임 설명은 8자 이상 500자 이하입니다.")
    private String description;

}
