package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.attendance.service.AttendanceReader;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.image.service.ImageReader;
import com.wemo.backend.domain.image.service.ImageStore;
import com.wemo.backend.domain.meeting.dto.MeetingCreateRequest;
import com.wemo.backend.domain.meeting.dto.MeetingDetailResponse;
import com.wemo.backend.domain.meeting.dto.MeetingUpdateRequest;
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
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.wemo.backend.global.exception.ErrorCode.ILLEGAL_MEETING_GRANTED;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final UserReader userReader;
    private final MeetingStore meetingStore;
    private final MeetingMemberStore meetingMemberStore;
    private final ImageStore imageStore;
    private final MeetingReader meetingReader;
    private final MeetingMemberReader meetingMemberReader;
    private final PlanReader planReader;
    private final ReviewReader reviewReader;
    private final ImageReader imageReader;
    private final AttendanceReader attendanceReader;

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
        User user = userReader.getUserByEmail(email);

        // 모임 객체 저장
        Meeting meeting = meetingStore.storeMeeting(request, user);

        // 모임 대표 이미지 저장
        Image image = imageStore.storeImage(user, meeting.getId(), request.getFileUrl(), Image.EntityType.MEETING);

        // 모임 생성자는 자동으로 가입
        meetingMemberStore.storeMemberToMeeting(user, meeting);

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

        meetingMemberStore.storeMemberToMeeting(user, meeting);

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
        String meetingImage = imageReader.getImage(meeting.getId(), Image.EntityType.MEETING);

        // 모임 멤버 정보 가져오기 (최근 가입한 6명까지)
        List<UserListInfo> userListInfoList = getUserListInfo(meeting)
                .stream()
                .sorted(Comparator.comparing(UserListInfo::getCreatedAt).reversed()) // 가입일 기준 내림차순 정렬
                .limit(6) // 상위 6명만 가져오기
                .collect(Collectors.toList());

        // 일정 정보 가져오기 (최근 3개까지)
        List<PlanListInfo> planListInfoList = getPlanListInfo(meeting)
                .stream()
                .sorted(Comparator.comparing(PlanListInfo::getDateTime).reversed()) // 일정 날짜 기준 내림차순 정렬
                .limit(3) // 상위 3개만 가져오기
                .collect(Collectors.toList());

        // 리뷰 정보 가져오기 (최근 2개까지)
        List<ReviewListInfo> reviewListInfoList = getReviewListInfo(planListInfoList)
                .stream()
                .sorted(Comparator.comparing(ReviewListInfo::getCreatedAt).reversed()) // 작성일 기준 내림차순 정렬
                .limit(2) // 상위 2개만 가져오기
                .collect(Collectors.toList());

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

    /**
     * 모임 정보 수정
     *
     * @param email 이메일
     * @param meetingId 모임 id
     * @param request 수정 요청 데이터 (모임 설명)
     * @return 성공 메세지
     */
    @Override
    @Transactional
    public String updateMeeting(String email, Long meetingId, MeetingUpdateRequest request) {

        User user = userReader.getUserByEmail(email);

        Meeting meeting = meetingReader.getMeeting(meetingId);

        if (!meeting.getUser().getEmail().equals(user.getEmail())) throw new CustomException(ILLEGAL_MEETING_GRANTED);

        meeting.updateDescription(request.getDescription());

        return "모임 내용이 수정되었습니다.";
    }

    @Override
    @Transactional
    public String deleteMeeting(String email, Long meetingId) {

        User user = userReader.getUserByEmail(email);

        Meeting meeting = meetingReader.getMeeting(meetingId);

        if (!meeting.getUser().getEmail().equals(user.getEmail())) {
            log.warn("사용자 {}가 권한 없이 모임 id {}를 삭제하려 했습니다.", user.getEmail(), meetingId);
            throw new CustomException(ILLEGAL_MEETING_GRANTED);
        }

        // 모임 삭제 처리
        meeting.softDelete();

        // 모임과 연관된 계획 정보 조회
        List<Plan> plans = planReader.getPlanByMeeting(meeting);
        plans.forEach(plan -> {
            // 계획에 속한 참석자 정보 삭제
            attendanceReader.getAttendanceList(plan).forEach(attendanceReader::delete);
            // 후기 정보 삭제
            reviewReader.getReviewByPlan(plan).forEach(review -> {
                // 후기와 관련된 이미지 삭제
                imageReader.deleteImage(review.getId(), Image.EntityType.REVIEW);
                reviewReader.delete(review);
            });
            // 계획에 속한 이미지 삭제
            imageReader.deleteImage(plan.getId(), Image.EntityType.PLAN);
            // 계획 정보 삭제
            planReader.delete(plan);
        });

        // 모임과 연관된 멤버 정보 삭제
        meetingMemberReader.getMemberListByMeeting(meeting).forEach(meetingMemberReader::delete);

        // 모임에 속한 이미지 삭제
        imageReader.deleteImage(meetingId, Image.EntityType.MEETING);

        return "모임이 삭제되었습니다.";
    }

    private List<UserListInfo> getUserListInfo(Meeting meeting) {
        return meetingMemberReader.getMemberListByMeeting(meeting).stream()
                .map(UserListInfo::fromEntity)
                .collect(Collectors.toList());
    }

    private List<PlanListInfo> getPlanListInfo(Meeting meeting) {
        List<Plan> planList = planReader.getPlanByMeeting(meeting);

        return planList.stream().map(plan -> {
            List<String> planImageList = imageReader.getImageList(plan.getId(), Image.EntityType.PLAN);

            return PlanListInfo.fromEntity(plan, attendanceReader.getParticipantsCount(plan), planImageList);
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
