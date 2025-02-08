package com.wemo.backend.domain.meeting.dto;

import com.wemo.backend.domain.meeting.entity.Meeting;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MeetingCreateResponse {

    private Long meetingId;

    private String meetingName;

    private String category;

    private String description;

    private List<String> meetingImagePath;

    public static MeetingCreateResponse fromEntity(Meeting meeting, List<String> meetingImagePath) {

        return MeetingCreateResponse.builder()
                .meetingId(meeting.getId())
                .meetingName(meeting.getMeetingName())
                .category(meeting.getCategory().getCategoryName())
                .description(meeting.getDescription())
                .meetingImagePath(meetingImagePath)
                .build();
    }

}
