package com.wemo.backend.domain.meeting.dto;

import com.wemo.backend.domain.meeting.entity.Meeting;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingInfoResponse {

    private Long meetingId;

    private String meetingName;

    private String description;

    private String meetingImagePath;

    private int memberCount;

    private double reviewAverage;

    public static MeetingInfoResponse fromEntity(Meeting meeting, int memberCount, double reviewAverage, String meetingImagePath) {

        return MeetingInfoResponse.builder()
                .meetingId(meeting.getId())
                .meetingName(meeting.getMeetingName())
                .description(meeting.getDescription())
                .meetingImagePath(meetingImagePath)
                .memberCount(memberCount)
                .reviewAverage(reviewAverage)
                .build();
    }

}
