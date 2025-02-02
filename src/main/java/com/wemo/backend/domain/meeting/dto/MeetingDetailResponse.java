package com.wemo.backend.domain.meeting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.dto.PlanListInfo;
import com.wemo.backend.domain.review.dto.ReviewListInfo;
import com.wemo.backend.domain.user.dto.UserListInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MeetingDetailResponse {

    private Long meetingId;

    private String meetingName;

    private List<String> meetingImagePath;

    private int memberCount;

    private String description;

    private String category;

    private String email;

    private String nickname;

    private String profileImagePath;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isJoined")
    private boolean isJoined;

    private List<UserListInfo> memberList;

    private int planCounts;

    private List<PlanListInfo> planList;

    private int reviewCounts;

    private double reviewAverage;

    private List<ReviewListInfo> reviewList;

    @JsonProperty("isJoined")
    public boolean getIsJoined() {

        return isJoined;
    }

    public static MeetingDetailResponse fromEntity(Meeting meeting, List<String> meetingImage, boolean isJoined, int memberCount, List<UserListInfo> userListInfoList, int planCounts, List<PlanListInfo> planListInfoList, int reviewCounts, double reviewAverage, List<ReviewListInfo> reviewListInfoList) {

        return MeetingDetailResponse.builder()
                .meetingId(meeting.getId())
                .meetingName(meeting.getMeetingName())
                .meetingImagePath(meetingImage)
                .memberCount(memberCount)
                .description(meeting.getDescription())
                .category(meeting.getCategory().getCategoryName())
                .email(meeting.getUser().getEmail())
                .nickname(meeting.getUser().getNickname())
                .profileImagePath(meeting.getUser().getProfileImagePath())
                .createdAt(meeting.getCreatedAt())
                .updatedAt(meeting.getUpdatedAt())
                .isJoined(isJoined)
                .memberList(userListInfoList)
                .planCounts(planCounts)
                .reviewAverage(reviewAverage)
                .planList(planListInfoList)
                .reviewCounts(reviewCounts)
                .reviewList(reviewListInfoList)
                .build();

    }

}
