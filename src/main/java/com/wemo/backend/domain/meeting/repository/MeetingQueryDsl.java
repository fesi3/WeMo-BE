package com.wemo.backend.domain.meeting.repository;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.user.dto.UserListInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingQueryDsl {

    Page<UserListInfo> getMemberListByMeeting(Meeting meeting, Pageable pageable);

}
