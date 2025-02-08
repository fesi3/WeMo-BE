package com.wemo.backend.domain.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MeetingListResponseV2 {

    private String email;

    private Long meetingId;

    private String meetingName;

    private String description;

    private String meetingImagePath;

    private Long memberCount;

    private String category;

    private Long planCount;

    private List<MeetingListPlanListResponse> planList;

    public MeetingListResponseV2(String email, Long meetingId, String meetingName, String description, String meetingImagePath, Long memberCount, String category, Long planCount) {

        this.email = email;
        this.meetingId = meetingId;
        this.meetingName = meetingName;
        this.description = description;
        this.meetingImagePath = meetingImagePath;
        this.memberCount = memberCount;
        this.category = category;
        this.planCount = planCount;
    }

    public void setPlanList(List<MeetingListPlanListResponse> planListResponses) {

        this.planList = planListResponses;

    }

}
