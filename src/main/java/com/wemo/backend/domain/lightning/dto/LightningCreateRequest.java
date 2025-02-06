package com.wemo.backend.domain.lightning.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LightningCreateRequest {

    @NotBlank(message = "모임명은 필수 입력 값입니다.")
    private String lightningName;

    @NotNull(message = "모임 타입 id는 필수 입력 값입니다.")
    @Min(value = 1, message = "모임 타입 id는 1 이상이어야 합니다.")
    private Long lightningTypeId;

    @NotNull(message = "모임 시간 id는 필수 입력 값입니다.")
    @Min(value = 1, message = "모임 시간 id는 1 이상이어야 합니다.")
    private Integer dateTypeId;

    @NotNull(message = "모임 날짜는 필수 입력 값입니다.")
    @Future(message = "모임 날짜는 현재 날짜 이후여야 합니다.")
    private LocalDateTime lightningDate;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotBlank(message = "모임 내용은 필수 입력 값입니다.")
    private String lightningContent;

    @NotNull(message = "위도 값은 필수 입력 값입니다.")
    private Double latitude;

    @NotNull(message = "경도 값은 필수 입력 값입니다.")
    private Double longitude;

    @NotNull(message = "모임 정원은 필수 입력 값입니다.")
    @Min(value = 2, message = "모임 정원은 최소 2명 이상이어야 합니다.")
    @Max(value = 50, message = "모임 정원은 최대 50명까지 가능합니다.")
    private Integer lightningCapacity;

}

