package com.wemo.backend.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserPlanListResponse {

    private String nickname;

    private String email;

    private String profileImagePath;

    private Long planId;

    private String planName;

    private String category;

    private Long meetingId;

    private String meetingName;

    private String address;

    private String province;

    private String district;

    private String planImagePath;

    private LocalDateTime dateTime;

    private LocalDateTime registrationEnd;

    private int capacity;

    private Long participants;

    private Long likeCount;

    private int viewCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isOpened")
    private boolean isOpened;

    @JsonProperty("isCanceled")
    private boolean isCanceled;

    @JsonProperty("isFulled")
    private boolean isFulled;

    @JsonProperty("isLiked")
    private boolean isLiked;

    @JsonProperty("isCanceled")
    public boolean getIsCanceled() {

        return isCanceled;
    }

    @JsonProperty("isOpened")
    public boolean getIsOpened() {

        return isOpened;
    }

    @JsonProperty("isFulled")
    public boolean getIsFulled() {

        return isFulled;
    }

    @JsonProperty("isLiked")
    public boolean getIsLiked() {

        return isLiked;
    }

}
