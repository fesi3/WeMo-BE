package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.dto.PlanListInfo;
import com.wemo.backend.domain.review.dto.ReviewListInfo;
import com.wemo.backend.domain.user.dto.UserListInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MeetingUtilService {

    List<UserListInfo> getTopUsers(Meeting meeting);

    List<PlanListInfo> getTopPlans(Meeting meeting);

    List<ReviewListInfo> getTopReviews(List<PlanListInfo> plans);

    void deletePlansAndReviews(Meeting meeting);

    void deleteMembers(Meeting meeting);

    void deleteMeetingImages(Meeting meeting);

}
