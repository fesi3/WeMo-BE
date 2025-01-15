package com.wemo.backend.domain.meeting.dto;

import com.wemo.backend.domain.meeting.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class MeetingReviewPagingResponse {

    private Long meetingId;

    private String meetingName;

    private int planCount;

    private List<MeetingReviewListResponse> planList;

    private int pageSize;

    private int page;

    private int totalPage;

    public MeetingReviewPagingResponse(Meeting meeting, Page<MeetingReviewListResponse> reviewListByMeeting) {

        this.meetingId = meeting.getId();
        this.meetingName = meeting.getMeetingName();
        this.planCount = reviewListByMeeting.getContent().size();
        this.planList = reviewListByMeeting.getContent();
        this.pageSize = reviewListByMeeting.getSize();
        this.page = reviewListByMeeting.getPageable().getPageNumber() + 1;
        this.totalPage = reviewListByMeeting.getTotalPages();

    }
}
