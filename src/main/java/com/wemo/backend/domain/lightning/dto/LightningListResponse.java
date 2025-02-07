package com.wemo.backend.domain.lightning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LightningListResponse {

    private Long lightningId;

    private String lightningName;

    private String lightningType;

    private String lightningTime;

    private LocalDateTime lightningDate;

    private int lightningCapacity;

    private Long lightningParticipants;

    private String address;

    private double latitude;

    private double longitude;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String nickname;

    private String profileImagePath;

    public void setLightningTime(String displayName) {

        this.lightningTime = displayName;

    }

}
