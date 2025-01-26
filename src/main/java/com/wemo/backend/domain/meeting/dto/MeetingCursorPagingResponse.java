package com.wemo.backend.domain.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MeetingCursorPagingResponse {

    private int meetingCount;

    private List<MeetingListResponse> meetingList;

    private Long nextCursor;

    public MeetingCursorPagingResponse(List<MeetingListResponse> meetingListResponses, int meetingCount) {

        this.meetingCount = meetingCount;
        this.meetingList = meetingListResponses;
        this.nextCursor = meetingListResponses.isEmpty() ? null : meetingListResponses.get(meetingListResponses.size() - 1).getMeetingId();

    }

}
