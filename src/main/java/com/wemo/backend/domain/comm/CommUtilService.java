package com.wemo.backend.domain.comm;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.user.dto.UserListInfo;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommUtilService {

    List<UserListInfo> getUserListInfo(Meeting meeting);

    double calculateReviewAverage(Meeting meeting);

    void validateMeetingOwner(User user, Meeting meeting);

}
