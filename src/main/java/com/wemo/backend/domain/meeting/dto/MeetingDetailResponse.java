package com.wemo.backend.domain.meeting.dto;

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

    private String meetingImagePath;

    private int memberCount;

    private String description;

    private String category;

    private String nickname;

    private String profileImagePath;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<UserListInfo> memberList;

    private int planCounts;

    private List<PlanListInfo> planList;

    private int reviewCounts;

    private List<ReviewListInfo> reviewList;

    public static MeetingDetailResponse fromEntity(Meeting meeting, String meetingImage, int memberCount, List<UserListInfo> userListInfoList, int planCounts, List<PlanListInfo> planListInfoList, int reviewCounts, List<ReviewListInfo> reviewListInfoList) {

        return MeetingDetailResponse.builder()
                .meetingId(meeting.getId())
                .meetingName(meeting.getMeetingName())
                .meetingImagePath(meetingImage)
                .memberCount(memberCount)
                .description(meeting.getDescription())
                .category(meeting.getCategory().getCategoryName())
                .nickname(meeting.getUser().getNickname())
                .profileImagePath(meeting.getUser().getProfileImagePath())
                .createdAt(meeting.getCreatedAt())
                .updatedAt(meeting.getUpdatedAt())
                .memberList(userListInfoList)
                .planCounts(planCounts)
                .planList(planListInfoList)
                .reviewCounts(reviewCounts)
                .reviewList(reviewListInfoList)
                .build();

    }

}
