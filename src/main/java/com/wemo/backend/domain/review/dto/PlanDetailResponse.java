package com.wemo.backend.domain.review.dto;

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

    private String profileImagePath;

    private String planName;

    private String category;

    private String address;

    private double longitude;

    private double latitude;

    private String planImagePath;

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

    private boolean isCanceled;

    private boolean isLiked;

    private boolean isOpened;

    private boolean isFulled;

    public static PlanDetailResponse fromEntity(Plan plan, String planImagePath, Meeting meeting, int participants, int likeCount, List<UserListInfo> userList, MeetingInfoResponse meetingInfoResponse, boolean isLiked) {

        return PlanDetailResponse.builder()
                .planId(plan.getId())
                .nickname(plan.getUser().getNickname())
                .profileImagePath(plan.getUser().getProfileImagePath())
                .planName(plan.getPlanName())
                .category(meeting.getCategory().getCategoryName())
                .address(plan.getAddress())
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
