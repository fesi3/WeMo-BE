package com.wemo.backend.domain.user.repository.querydsl;

import com.wemo.backend.domain.user.dto.UserMeetingListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryDsl {

    Page<UserMeetingListResponse> getUserMeetingList(String email, Pageable pageable);

}
