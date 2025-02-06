package com.wemo.backend.domain.lightning.dto;

import com.wemo.backend.domain.lightning.entity.Lightning;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class LightningCreateResponse {

    private String lightningName;

    private String lightningType;

    private String lightningTime;

    private LocalDateTime lightningDate;

    private String address;

    private String lightningContent;

    private double latitude;

    private double longitude;

    private int lightningCapacity;

    public static LightningCreateResponse fromEntity(Lightning lightning) {

        return LightningCreateResponse.builder()
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
