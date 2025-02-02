package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.attendance.service.AttendanceReader;
import com.wemo.backend.domain.comm.CommUtilService;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.image.service.ImageReader;
import com.wemo.backend.domain.image.service.ImageStore;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meetingMember.service.MeetingMemberReader;
import com.wemo.backend.domain.meetingMember.service.MeetingMemberStore;
import com.wemo.backend.domain.plan.dto.PlanListInfo;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.service.PlanReader;
import com.wemo.backend.domain.review.dto.ReviewListInfo;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.review.service.ReviewReader;
import com.wemo.backend.domain.user.dto.UserListInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingUtilServiceImpl implements MeetingUtilService {

    private final CommUtilService commUtilService;

    private final PlanReader planReader;

    private final ReviewReader reviewReader;

    private final ImageReader imageReader;

    private final ImageStore imageStore;

    private final MeetingMemberReader meetingMemberReader;

    private final AttendanceReader attendanceReader;

    private final MeetingMemberStore meetingMemberStore;

    private static final Comparator<UserListInfo> USER_DATE_DESC = Comparator.comparing(UserListInfo::getCreatedAt).reversed();
    private static final Comparator<PlanListInfo> PLAN_DATE_DESC = Comparator.comparing(PlanListInfo::getDateTime).reversed();
    private static final Comparator<ReviewListInfo> REVIEW_DATE_DESC = Comparator.comparing(ReviewListInfo::getCreatedAt).reversed();

    /**
     * 모임의 최근 가입자 6명 조회
     *
     * @param meeting 모임 객체
     * @return 최근 가입자 목록
     */
    @Override
    public List<UserListInfo> getTopUsers(Meeting meeting) {

        return commUtilService.getUserListInfo(meeting).stream()
                .sorted(USER_DATE_DESC)
                .limit(6)
                .collect(Collectors.toList());
    }

    /**
     * 모임의 최근 3개 일정 조회
     *
     * @param meeting 모임 객체
     * @return 최근 일정 목록
     */
    @Override
    public List<PlanListInfo> getTopPlans(Meeting meeting) {

        return getPlanListInfo(meeting).stream()
                .sorted(PLAN_DATE_DESC)
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * 모임 일정에 대한 최근 2개 후기 조회
     *
     * @param plans 일정 목록
     * @return 최근 후기 목록
     */
    @Override
    public List<ReviewListInfo> getTopReviews(List<PlanListInfo> plans) {

        return getReviewListInfo(plans).stream()
                .sorted(REVIEW_DATE_DESC)
                .limit(2)
                .collect(Collectors.toList());
    }

    /**
     * 모임에 대한 모든 일정과 후기, 이미지를 삭제
     *
     * @param meeting 모임 객체
     */
    @Override
    public void deletePlansAndReviews(Meeting meeting) {

        List<Plan> plans = planReader.getPlanByMeeting(meeting);

        for (Plan plan : plans) {
            deleteReviews(plan);
            deletePlanImages(plan);
            plan.softDelete();
        }
    }

    /**
     * 일정에 대한 모든 후기를 삭제
     *
     * @param plan 일정 객체
     */
    private void deleteReviews(Plan plan) {

        List<Review> reviews = reviewReader.getReviewByPlan(plan);

        for (Review review : reviews) {
            deleteReviewImages(review);
            review.softDelete();
        }
    }

    /**
     * 리뷰에 대한 이미지를 삭제
     *
     * @param review 리뷰 객체
     */
    private void deleteReviewImages(Review review) {

        imageStore.deleteImage(review.getId(), Image.EntityType.REVIEW);
    }

    /**
     * 일정에 대한 이미지를 삭제
     *
     * @param plan 일정 객체
     */
    private void deletePlanImages(Plan plan) {

        imageStore.deleteImage(plan.getId(), Image.EntityType.PLAN);
    }

    /**
     * 모임에 대한 모든 멤버 데이터를 삭제
     *
     * @param meeting 모임 객체
     */
    @Override
    public void deleteMembers(Meeting meeting) {

        meetingMemberReader.getMemberListByMeeting(meeting)
                .forEach(meetingMemberStore::deleteMeetingMember);
    }

    /**
     * 모임에 대한 이미지를 삭제
     *
     * @param meeting 모임 객체
     */
    @Override
    public void deleteMeetingImages(Meeting meeting) {

        imageStore.deleteImage(meeting.getId(), Image.EntityType.MEETING);
    }

    /**
     * 모임의 전체 일정 목록 조회
     *
     * @param meeting 모임 객체
     * @return 일정 목록
     */
    private List<PlanListInfo> getPlanListInfo(Meeting meeting) {

        return planReader.getPlanByMeeting(meeting).stream()
                .map(plan -> {
                    List<String> planImageList = imageReader.getImageList(plan.getId(), Image.EntityType.PLAN);
                    return PlanListInfo.fromEntity(plan, attendanceReader.getParticipantsCount(plan), planImageList);
                })
                .collect(Collectors.toList());
    }

    /**
     * 모임 일정에 대한 후기 목록 반환
     *
     * @param planListInfoList 일정 목록
     * @return 후기 목록
     */
    private List<ReviewListInfo> getReviewListInfo(List<PlanListInfo> planListInfoList) {

        List<ReviewListInfo> reviewListInfoList = new ArrayList<>();
        for (PlanListInfo planListInfo : planListInfoList) {
            Plan plan = planReader.getPlan(planListInfo.getPlanId());
            reviewReader.getReviewByPlan(plan).forEach(review -> reviewListInfoList.add(ReviewListInfo.fromEntity(review)));
        }
        return reviewListInfoList;
    }

}
