package com.wemo.backend.domain.plan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlanCreateRequest {

    @NotBlank(message = "일정명은 필수 입력 값입니다.")
    @Size(min = 3, max = 50, message = "일정명은 3자 이상 50자 이내입니다.")
    private String planName;

    @Size(max = 500, message = "모임 설명은 500자 이내입니다.")
    private String content;

    @NotBlank(message = "모임 날짜는 필수 입력 값입니다.")
    private String dateTime;

    @NotBlank(message = "마감 날짜는 필수 입력 값입니다.")
    private String registrationEnd;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotNull(message = "경도는 필수 입력 값입니다.")
    private double longitude;

    @NotNull(message = "위도는 필수 입력 값입니다.")
    private double latitude;

    @Min(value = 2, message = "모집 정원은 최소 2명입니다.")
    @Max(value = 100, message = "모집 정원은 최대 100명입니다.")
    private int capacity;

    private String fileUrl;

}
