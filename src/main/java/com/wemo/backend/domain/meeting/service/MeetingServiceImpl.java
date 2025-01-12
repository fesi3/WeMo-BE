package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.image.service.ImageReader;
import com.wemo.backend.domain.image.service.ImageStore;
import com.wemo.backend.domain.meeting.dto.MeetingCreateRequest;
import com.wemo.backend.domain.meeting.dto.MeetingDetailResponse;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.dto.PlanListInfo;
import com.wemo.backend.domain.plan.entity.Attendance;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.service.PlanReader;
import com.wemo.backend.domain.review.dto.ReviewListInfo;
import com.wemo.backend.domain.review.entity.Review;
import com.wemo.backend.domain.review.service.ReviewReader;
import com.wemo.backend.domain.user.dto.UserListInfo;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final UserReader userReader;
    private final MeetingStore meetingStore;
    private final ImageStore imageStore;
    private final MeetingReader meetingReader;
    private final PlanReader planReader;
    private final ReviewReader reviewReader;
    private final ImageReader imageReader;

    /**
     * 0. 모임 생성
     *
     * @param email 이메일
     * @param request 모임 생성 데이터 (모임명, 모임 설명, 카테고리, 모임 이미지)
     */
    @Override
    @Transactional
    public void createMeeting(String email, MeetingCreateRequest request) {

        // 유저 객체 검증
        User userByEmail = userReader.getUserByEmail(email);

        // 모임 객체 저장
        Meeting meeting = meetingStore.storeMeeting(request, userByEmail);

        // 모임 대표 이미지 저장
        imageStore.storeImage(userByEmail, meeting.getId(), request.getFileUrl(), Image.EntityType.MEETING);

        // 모임 생성자는 자동으로 가입
        meetingStore.joinMeeting(userByEmail, meeting);

    }

    /**
     * 모임 가입
     *
     * @param email 이메일
     * @param meetingId 모임 id
     * @return 성공 응답 메세지
     */
    @Override
    @Transactional
    public String joinMeeting(String email, Long meetingId) {

        User user = userReader.getUserByEmail(email);
        Meeting meeting = meetingReader.getMeeting(meetingId);

        meetingStore.joinMeeting(user, meeting);

        return "모임 가입 성공";
    }

    /**
     * 모임 상세 조회
     *
     * @param meetingId 모임 id
     * @return 모임에 관련된 모든 정보 반환 (모임 정보, 멤버 리스트, 일정 리스트, 후기 리스트)
     */
    @Override
    public MeetingDetailResponse getMeetingDetail(Long meetingId) {

        // 모임 유효성 검사
        Meeting meeting = meetingReader.getMeeting(meetingId);

        // 모임 이미지 가져오기
        Image meetingImage = imageReader.getImage(meeting.getId(), Image.EntityType.MEETING);

        // 모임 멤버 정보 가져오기
        List<UserListInfo> userListInfoList = getUserListInfo(meeting);

        // 일정 정보 가져오기
        List<PlanListInfo> planListInfoList = getPlanListInfo(meeting);

        // 리뷰 정보 가져오기
        List<ReviewListInfo> reviewListInfoList = getReviewListInfo(planListInfoList);

        return MeetingDetailResponse.fromEntity(
                meeting,
                meetingImage,
                userListInfoList.size(),
                userListInfoList,
                planListInfoList.size(),
                planListInfoList,
                reviewListInfoList.size(),
                reviewListInfoList
        );
    }

    private List<UserListInfo> getUserListInfo(Meeting meeting) {
        return meetingReader.getMemberByMeeting(meeting).stream()
                .map(UserListInfo::fromEntity)
                .collect(Collectors.toList());
    }

    private List<PlanListInfo> getPlanListInfo(Meeting meeting) {
        List<Plan> planList = planReader.getPlanByMeeting(meeting);

        return planList.stream().map(plan -> {
            List<Attendance> attendanceList = planReader.getAttendanceList(plan);
            Image planImage = imageReader.getImage(plan.getId(), Image.EntityType.PLAN);

            return PlanListInfo.fromEntity(plan, attendanceList.size(), planImage);
        }).collect(Collectors.toList());
    }

    private List<ReviewListInfo> getReviewListInfo(List<PlanListInfo> planListInfoList) {
        List<ReviewListInfo> reviewListInfoList = new ArrayList<>();

        for (PlanListInfo planListInfo : planListInfoList) {
            Plan plan = planReader.getPlan(planListInfo.getPlanId());
            List<Review> reviews = reviewReader.getReviewByPlan(plan);

            reviews.forEach(review -> {
                ReviewListInfo reviewListInfo = ReviewListInfo.fromEntity(review);
                reviewListInfoList.add(reviewListInfo);
            });
        }

        return reviewListInfoList;
    }

}
