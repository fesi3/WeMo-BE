package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.attendance.entity.Attendance;
import com.wemo.backend.domain.attendance.service.AttendanceReader;
import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.image.service.ImageReader;
import com.wemo.backend.domain.image.service.ImageStore;
import com.wemo.backend.domain.like.repository.LikeRepository;
import com.wemo.backend.domain.meeting.dto.MeetingInfoResponse;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meeting.service.MeetingReader;
import com.wemo.backend.domain.meetingMember.service.MeetingMemberStore;
import com.wemo.backend.domain.plan.dto.PlanCreateRequest;
import com.wemo.backend.domain.plan.dto.PlanCreateResponse;
import com.wemo.backend.domain.plan.dto.PlanCursorPagingResponse;
import com.wemo.backend.domain.plan.dto.PlanDetailResponse;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.repository.PlanRepository;
import com.wemo.backend.domain.region.entity.District;
import com.wemo.backend.domain.region.entity.Province;
import com.wemo.backend.domain.region.repository.DistrictRepository;
import com.wemo.backend.domain.region.repository.ProvinceRepository;
import com.wemo.backend.domain.region.service.RegionServiceImpl;
import com.wemo.backend.domain.user.dto.UserListInfo;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wemo.backend.global.exception.ErrorCode.ILLEGAL_MEETING_GRANTED;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final UserReader userReader;

    private final MeetingReader meetingReader;

    private final ProvinceRepository provinceRepository;

    private final DistrictRepository districtRepository;

    private final ImageStore imageStore;

    private final PlanStore planStore;

    private final PlanReader planReader;

    private final PlanRepository planRepository;

    private final LikeRepository likeRepository;

    private final ImageReader imageReader;

    private final AttendanceReader attendanceReader;

    private final MeetingMemberStore meetingMemberStore;

    /**
     * 0. 일정 생성
     *
     * @param email 이메일
     * @param request 일정 생성 요청 데이터
     * @param meetingId 모임 id
     * @return 생성된 일정 정보 반환
     */
    @Override
    @Transactional
    public PlanCreateResponse createPlan(String email, PlanCreateRequest request, Long meetingId) {

        // 유저 객체 검증
        User user = userReader.getUserByEmail(email);

        // 모임장인지 검증
        Meeting meeting = meetingReader.getMeeting(meetingId);
        if (user.getId() != meeting.getUser().getId()) throw new CustomException(ILLEGAL_MEETING_GRANTED);

        // 주소 저장
        // Step 1: 주소 파싱
        Map<String, String> parsedAddress = RegionServiceImpl.parseAddress(request.getAddress());
        String provinceName = parsedAddress.get("province");
        String districtName = parsedAddress.get("district");

        // Step 2: 시/도 저장 또는 조회
        Province province = provinceRepository.findByProvinceName(provinceName)
                .orElseGet(() -> provinceRepository.save(new Province(provinceName)));

        // Step 3: 군/구 저장 또는 조회
        District district = districtRepository.findByDistrictNameAndProvince(districtName, province)
                .orElseGet(() -> districtRepository.save(new District(districtName, province)));

        Plan plan = planStore.storePlan(request, district, user, meeting);

        // 주최자는 일정에 자동으로 참여
        planStore.joinPlan(user, plan);

        // 이미지 저장 (선택)
        if (!request.getFileUrls().isEmpty()) {

            List<String> imageList = imageStore.storeImageList(user, plan.getId(), request.getFileUrls(), Image.EntityType.PLAN);
            return PlanCreateResponse.fromEntityWithImage(plan, meeting, imageList);
        }

        return PlanCreateResponse.fromEntity(plan, meeting);
    }

    /**
     * 일정 참여
     *
     * @param email 이메일
     * @param planId 일정 id
     * @return 성공 응답 메세지
     */
    @Override
    @Transactional
    public String joinPlan(String email, Long planId) {

        User user = userReader.getUserByEmail(email);
        Plan plan = planReader.getPlan(planId);

        planStore.joinPlan(user, plan);

        // 일정 참여 시 모임에도 자동으로 가입
        meetingMemberStore.storeMemberToMeeting(user, plan.getMeeting());

        return "일정 참여 신청 완료되었습니다.";
    }

    /**
     * 일정 목록 조회
     *
     * @param userDetails 유저 정보 객체
     * @param cursor      커서 id
     * @param size        조회 요청 개수
     * @param province    시/도 데이터
     * @param district    군/구 데이터
     * @param startDate   시작 날짜
     * @param endDate     끝 날짜
     * @param categoryId  카테고리 id
     * @param sort        정렬 기준
     * @return 조건에 해당하는 일정 목록
     */
    @Override
    @Transactional
    public PlanCursorPagingResponse getPlanList(UserDetailsImpl userDetails, Long cursor, int size, String query, String province, String district, String startDate, String endDate, Long categoryId, String sort) {

        PlanCursorPagingResponse response;

        if (userDetails != null && !userDetails.isGuest()) {
            // 회원인 경우
            response = planRepository.getPlanListByUser(userDetails.getUsername(), cursor, size, query, province, district, startDate, endDate, categoryId, sort);
        } else {
            // 비회원인 경우
            response = planRepository.getPlanListByGuest(cursor, size, query, province, district, startDate, endDate, categoryId, sort);
        }

        return response;
    }

    /**
     * 일정 상세 조회
     *
     * @param userDetails 유저 정보 객체
     * @param planId 일정 id
     * @return 요청한 일정의 상세 정보
     */
    @Override
    @Transactional
    public PlanDetailResponse getPlanDetail(UserDetailsImpl userDetails, Long planId) {

        // 일정 유효성 검사
        Plan plan = planReader.getPlan(planId);

        // 일정 이미지 URL 조회
        List<String> planImageUrl = imageReader.getImageList(planId, Image.EntityType.PLAN);

        // 일정 참여자 및 좋아요 정보 조회
        List<UserListInfo> userList = getUserListFromAttendance(plan);
        int participants = userList.size();

        int likeCount = likeRepository.countByPlan(plan);
        boolean isLiked = isPlanLikedByUser(userDetails, plan);

        // 일정으로 모임 정보 생성
        MeetingInfoResponse meetingInfoResponse = getMeetingInfoResponse(plan);

        plan.updateViewCount();

        // PlanDetailResponse 생성 및 반환
        return PlanDetailResponse.fromEntity(plan, planImageUrl, plan.getMeeting(), participants, likeCount, userList, meetingInfoResponse, isLiked);
    }

    /**
     * 일정 모집 취소
     *
     * @param email 이메일
     * @param planId 일정 id
     * @return 성공 메세지
     */
    @Override
    @Transactional
    public String cancelPlan(String email, Long planId) {

        User user = userReader.getUserByEmail(email);

        Plan plan = planReader.getPlan(planId);

        if (!plan.getUser().getEmail().equals(user.getEmail())) throw new CustomException(ILLEGAL_MEETING_GRANTED);

        plan.cancel();

        return "일정이 정상적으로 취소되었습니다.";
    }

    private List<UserListInfo> getUserListFromAttendance(Plan plan) {

        List<Attendance> attendanceList = attendanceReader.getAttendanceList(plan);
        return attendanceList.stream()
                .map(UserListInfo::fromEntity)
                .collect(Collectors.toList());
    }

    private boolean isPlanLikedByUser(UserDetailsImpl userDetails, Plan plan) {

        if (userDetails == null || userDetails.isGuest()) {
            return false;
        }

        User user = userReader.getUserByEmail(userDetails.getUsername());
        return likeRepository.existsByUserAndPlan(user, plan);
    }

    private MeetingInfoResponse getMeetingInfoResponse(Plan plan) {

        Meeting meeting = plan.getMeeting();
        String meetingImageUrl = imageReader.getImage(meeting.getId(), Image.EntityType.MEETING);
        return MeetingInfoResponse.fromEntity(meeting, meetingImageUrl);
    }

}
