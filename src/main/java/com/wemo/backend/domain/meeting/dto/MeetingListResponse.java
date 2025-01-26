package com.wemo.backend.domain.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingListResponse {

    private String email;

    private Long meetingId;

    private String meetingName;

    private String description;

    private String meetingImagePath;

    private Long memberCount;

    private String category;

}
