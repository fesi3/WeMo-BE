package com.wemo.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserMeetingPagingResponse {

    private int meetingCount;

    private List<UserMeetingListResponse> meetingList;

    private int pageSize;

    private int page;

    private int totalPage;

    public UserMeetingPagingResponse(Page<UserMeetingListResponse> meetingList) {

        this.meetingCount = (int) meetingList.getTotalElements();
        this.meetingList = meetingList.getContent();
        this.pageSize = meetingList.getSize();
        this.page = meetingList.getPageable().getPageNumber() + 1;
        this.totalPage = meetingList.getTotalPages();

    }

}
