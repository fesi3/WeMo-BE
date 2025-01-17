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

    private int reviewCount;

    private List<MeetingReviewListResponse> reviewList;

    private int pageSize;

    private int page;

    private int totalPage;

    public MeetingReviewPagingResponse(Meeting meeting, Page<MeetingReviewListResponse> reviewListByMeeting) {

        this.meetingId = meeting.getId();
        this.meetingName = meeting.getMeetingName();
        this.reviewCount = reviewListByMeeting.getContent().size();
        this.reviewList = reviewListByMeeting.getContent();
        this.pageSize = reviewListByMeeting.getSize();
        this.page = reviewListByMeeting.getPageable().getPageNumber() + 1;
        this.totalPage = reviewListByMeeting.getTotalPages();

    }

}
