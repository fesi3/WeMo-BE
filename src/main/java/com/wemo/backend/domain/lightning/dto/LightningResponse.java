package com.wemo.backend.domain.lightning.dto;

import com.wemo.backend.domain.lightning.entity.Lightning;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class LightningResponse {

    private Long lightningId;

    private String lightningName;

    private String lightningType;

    private String lightningTime;

    private LocalDateTime lightningDate;

    private String address;

    private String lightningContent;

    private double latitude;

    private double longitude;

    private int lightningCapacity;

    public static LightningResponse fromEntity(Lightning lightning) {

        return LightningResponse.builder()
                .lightningId(lightning.getId())
                .lightningName(lightning.getLightningName())
                .lightningType(lightning.getLightningType().getLightTypeName())
                .lightningTime(lightning.getDateType().getName())
                .lightningDate(lightning.getLightningDate())
                .address(lightning.getAddress())
                .lightningContent(lightning.getLightningContent())
                .latitude(lightning.getLatitude())
                .longitude(lightning.getLongitude())
                .lightningCapacity(lightning.getLightningCapacity())
                .build();
    }

}
