package com.wemo.backend.domain.meeting.dto;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.user.dto.UserListInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MeetingMemberPagingResponse {

    private Long meetingId;

    private String meetingName;

    private int memberCount;

    private List<UserListInfo> memberList;

    private int pageSize;

    private int page;

    private int totalPage;

    public MeetingMemberPagingResponse(Meeting meeting, Page<UserListInfo> memberListByMeeting) {

        this.meetingId = meeting.getId();
        this.meetingName = meeting.getMeetingName();
        this.memberCount = memberListByMeeting.getContent().size();
        this.memberList = memberListByMeeting.getContent();
        this.pageSize = memberListByMeeting.getSize();
        this.page = memberListByMeeting.getPageable().getPageNumber() + 1;
        this.totalPage = memberListByMeeting.getTotalPages();

    }

}
