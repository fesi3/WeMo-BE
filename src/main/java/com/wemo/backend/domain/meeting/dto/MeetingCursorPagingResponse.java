package com.wemo.backend.domain.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MeetingCursorPagingResponse {

    private int meetingCount;

    private List<?> meetingList;

    private Long nextCursor;

    public MeetingCursorPagingResponse(List<?> meetingListResponses, int meetingCount) {

        this.meetingCount = meetingCount;
        this.meetingList = meetingListResponses;
        this.nextCursor = meetingListResponses.isEmpty() ? null : getLastMeetingId(meetingListResponses);
    }

    private static Long getLastMeetingId(List<?> meetingListResponses) {

        if (!meetingListResponses.isEmpty()) {
            Object lastItem = meetingListResponses.get(meetingListResponses.size() - 1);
            if (lastItem instanceof MeetingListResponse) {
                return ((MeetingListResponse) lastItem).getMeetingId();
            } else if (lastItem instanceof MeetingListResponseV2) {
                return ((MeetingListResponseV2) lastItem).getMeetingId();
            }
        }
        return null;
    }

}
