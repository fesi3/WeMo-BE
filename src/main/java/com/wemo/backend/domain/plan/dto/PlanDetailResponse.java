package com.wemo.backend.domain.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wemo.backend.domain.meeting.dto.MeetingInfoResponse;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.dto.UserListInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PlanDetailResponse {

    private Long planId;

    private String nickname;

    private String email;

    private String profileImagePath;

    private String planName;

    private String category;

    private String address;

    private String addressDetail;

    private double longitude;

    private double latitude;

    private List<String> planImagePath;

    private String content;

    private LocalDateTime dateTime;

    private LocalDateTime registrationEnd;

    private int capacity;

    private int participants;

    private int likeCount;

    private int viewCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<UserListInfo> userList;

    private MeetingInfoResponse meetingInfo;

    @JsonProperty("isJoined")
    private boolean isJoined;

    @JsonProperty("isCanceled")
    private boolean isCanceled;

    @JsonProperty("isLiked")
    private boolean isLiked;

    @JsonProperty("isOpened")
    private boolean isOpened;

    @JsonProperty("isFulled")
    private boolean isFulled;

    @JsonProperty("isJoined")
    public boolean getIsJoined() {

        return isJoined;
    }

    @JsonProperty("isCanceled")
    public boolean getIsCanceled() {

        return isCanceled;
    }

    @JsonProperty("isLiked")
    public boolean getIsLiked() {

        return isLiked;
    }

    @JsonProperty("isOpened")
    public boolean getIsOpened() {

        return isOpened;
    }

    @JsonProperty("isFulled")
    public boolean getIsFulled() {

        return isFulled;
    }

    public static PlanDetailResponse fromEntity(boolean isJoined, Plan plan, List<String> planImagePath, Meeting meeting, int participants, int likeCount, List<UserListInfo> userList, MeetingInfoResponse meetingInfoResponse, boolean isLiked) {

        return PlanDetailResponse.builder()
                .planId(plan.getId())
                .nickname(plan.getUser().getNickname())
                .email(plan.getUser().getEmail())
                .profileImagePath(plan.getUser().getProfileImagePath())
                .isJoined(isJoined)
                .planName(plan.getPlanName())
                .category(meeting.getCategory().getCategoryName())
                .address(plan.getAddress())
                .addressDetail(plan.getAddressDetail())
                .longitude(plan.getLongitude())
                .latitude(plan.getLatitude())
                .planImagePath(planImagePath)
                .content(plan.getContent())
                .dateTime(plan.getDateTime())
                .registrationEnd(plan.getRegistrationEnd())
                .capacity(plan.getCapacity())
                .participants(participants)
                .likeCount(likeCount)
                .viewCount(plan.getViewCount())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .userList(userList)
                .meetingInfo(meetingInfoResponse)
                .isCanceled(plan.isCanceled())
                .isLiked(isLiked)
                .isOpened(plan.isOpened())
                .isFulled(plan.isFulled())
                .build();

    }

}
