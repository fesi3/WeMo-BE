package com.wemo.backend.domain.meeting.dto;

import com.wemo.backend.domain.meeting.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class MeetingPlanPagingResponse {

    private Long meetingId;

    private String meetingName;

    private int planCount;

    private List<MeetingPlanListResponse> planList;

    private int pageSize;

    private int page;

    private int totalPage;

    public MeetingPlanPagingResponse(Meeting meeting, Page<MeetingPlanListResponse> planListByMeeting) {

        this.meetingId = meeting.getId();
        this.meetingName = meeting.getMeetingName();
        this.planCount = planListByMeeting.getContent().size();
        this.planList = planListByMeeting.getContent();
        this.pageSize = planListByMeeting.getSize();
        this.page = planListByMeeting.getPageable().getPageNumber() + 1;
        this.totalPage = planListByMeeting.getTotalPages();

    }

}
