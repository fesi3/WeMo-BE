package com.wemo.backend.domain.lightning.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wemo.backend.domain.lightning.entity.Lightning;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class LightningDetailResponse {

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

    private String email;

    private String nickname;

    private String profileImagePath;

    private boolean isJoined;

    @JsonProperty("isJoined")
    public boolean getIsJoined() {

        return isJoined;
    }

    public static LightningDetailResponse fromEntity(Lightning lightning, boolean isJoined) {

        return LightningDetailResponse.builder()
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
                .email(lightning.getUser().getEmail())
                .nickname(lightning.getUser().getNickname())
                .profileImagePath(lightning.getUser().getProfileImagePath())
                .isJoined(isJoined)
                .build();
    }

}
