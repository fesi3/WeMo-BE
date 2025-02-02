package com.wemo.backend.domain.comm;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meetingMember.service.MeetingMemberReader;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.service.PlanReader;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.review.service.ReviewReader;
import com.wemo.backend.domain.user.dto.UserListInfo;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.wemo.backend.global.exception.ErrorCode.MEETING_PERMISSION_DENIED;

@Service
@RequiredArgsConstructor
public class CommUtilServiceImpl implements CommUtilService {

    private final PlanReader planReader;

    private final ReviewReader reviewReader;

    private final MeetingMemberReader meetingMemberReader;

    /**
     * 모임의 전체 멤버 목록 반환
     *
     * @param meeting 모임 객체
     * @return 멤버 목록
     */
    public List<UserListInfo> getUserListInfo(Meeting meeting) {

        return meetingMemberReader.getMemberListByMeeting(meeting).stream()
                .map(UserListInfo::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 모임에 대한 평균 평점 계산
     *
     * @param meeting 모임 객체
     * @return 평균 평점
     */
    @Override
    public double calculateReviewAverage(Meeting meeting) {

        List<Plan> plans = planReader.getPlanByMeeting(meeting);
        if (plans.isEmpty()) {
            return 0;
        }
        double totalScore = 0;
        int totalReviews = 0;
        for (Plan plan : plans) {
            List<Review> reviews = reviewReader.getReviewByPlan(plan);
            for (Review review : reviews) {
                totalScore += review.getScore();
                totalReviews++;
            }
        }
        return totalReviews == 0 ? 0 : totalScore / totalReviews;
    }

    /**
     * 모임의 소유자가 요청한 사용자인지 검증
     *
     * @param user    사용자 객체
     * @param meeting 모임 객체
     * @throws CustomException 권한이 없는 경우 예외 발생
     */
    @Override
    public void validateMeetingOwner(User user, Meeting meeting) {

        if (!meeting.getUser().getEmail().equals(user.getEmail())) {
            throw new CustomException(MEETING_PERMISSION_DENIED);
        }
    }

}
