package com.wemo.backend.domain.meeting.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingCreateRequest {

    @NotBlank(message = "모임명은 필수 입력 값입니다.")
    @Size(min = 2, max = 30, message = "모임명은 2자 이상 30자 이하입니다.")
    private String meetingName;

    @Size(min = 8, max = 500, message = "모임 설명은 8자 이상 500자 이하입니다.")
    private String description;

    @NotNull(message = "카테고리 id는 필수 입력 값입니다.")
    // @PositiveOrZero : 값이 양수 또는 0인지 검증
    @PositiveOrZero(message = "카테고리 id는 양수 값이어야 합니다.")
    private Long categoryId;

    @NotBlank(message = "모임 대표 이미지는 필수 입력 값입니다.")
    private String fileUrl;

}
