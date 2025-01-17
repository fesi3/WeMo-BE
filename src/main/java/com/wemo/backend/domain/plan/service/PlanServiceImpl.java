package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.attendance.entity.Attendance;
import com.wemo.backend.domain.attendance.service.AttendanceReader;
import com.wemo.backend.domain.attendance.service.AttendanceStore;
import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.comm.CommUtilService;
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
import com.wemo.backend.domain.user.dto.UserPlanPagingResponse;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final AttendanceStore attendanceStore;
    private final MeetingMemberStore meetingMemberStore;
    private final CommUtilService commUtilService;

    /**
     * 일정 생성
     *
     * @param email     사용자 이메일
     * @param request   일정 생성 요청 데이터
     * @param meetingId 모임 id
     * @return 생성된 일정 정보 반환
     */
    @Override
    @Transactional
    public PlanCreateResponse createPlan(String email, PlanCreateRequest request, Long meetingId) {

        User user = userReader.getUserByEmail(email);
        Meeting meeting = meetingReader.getMeeting(meetingId);

        commUtilService.validateMeetingOwner(user, meeting);

        Map<String, String> parsedAddress = RegionServiceImpl.parseAddress(request.getAddress());
        Province province = getOrSaveProvince(parsedAddress.get("province"));
        District district = getOrSaveDistrict(parsedAddress.get("district"), province);

        Plan plan = planStore.storePlan(request, district, user, meeting);
        log.info("일정 id {} 저장 완료", plan.getId());

        attendanceStore.attendPlan(user, plan);

        if (!request.getFileUrls().isEmpty()) {
            List<String> imageList = imageStore.storeImageList(user, plan.getId(), request.getFileUrls(), Image.EntityType.PLAN);
            return PlanCreateResponse.fromEntityWithImage(plan, meeting, imageList);
        }

        return PlanCreateResponse.fromEntity(plan, meeting);
    }

    private Province getOrSaveProvince(String provinceName) {

        return provinceRepository.findByProvinceName(provinceName)
                .orElseGet(() -> provinceRepository.save(new Province(provinceName)));
    }

    private District getOrSaveDistrict(String districtName, Province province) {

        return districtRepository.findByDistrictNameAndProvince(districtName, province)
                .orElseGet(() -> districtRepository.save(new District(districtName, province)));
    }

    /**
     * 일정에 참여
     *
     * @param email  사용자 이메일
     * @param planId 일정 id
     * @return 일정 참여 성공 메시지
     */
    @Override
    @Transactional
    public String joinPlan(String email, Long planId) {

        User user = userReader.getUserByEmail(email);
        Plan plan = planReader.getPlan(planId);

        attendanceStore.attendPlan(user, plan);
        log.info("사용자 {}가 일정 id {}에 참여했습니다.", user.getEmail(), plan.getId());

        meetingMemberStore.forceJoinMeeting(user, plan.getMeeting());
        log.info("사용자 {}가 모임 id {}에 자동 가입되었습니다.", user.getEmail(), plan.getMeeting().getId());

        return "일정 참여 신청 완료되었습니다.";
    }

    /**
     * 일정 목록 조회
     *
     * @param userDetails 유저 정보 객체
     * @param cursor      커서 id
     * @param size        조회 요청 개수
     * @param province    시/도
     * @param district    군/구
     * @param startDate   시작 날짜
     * @param endDate     끝 날짜
     * @param categoryId  카테고리 id
     * @param sort        정렬 기준
     * @return 조건에 해당하는 일정 목록
     */
    @Override
    @Transactional
    public PlanCursorPagingResponse getPlanList(UserDetailsImpl userDetails, Long cursor, int size, String query, String province, String district, String startDate, String endDate, Long categoryId, String sort) {

        return userDetails != null && !userDetails.isGuest()
                ? planRepository.getPlanListByUser(userDetails.getUsername(), cursor, size, query, province, district, startDate, endDate, categoryId, sort)
                : planRepository.getPlanListByGuest(cursor, size, query, province, district, startDate, endDate, categoryId, sort);
    }

    /**
     * 일정 상세 조회
     *
     * @param userDetails 유저 정보 객체
     * @param planId      일정 id
     * @return 일정의 상세 정보
     */
    @Override
    @Transactional
    public PlanDetailResponse getPlanDetail(UserDetailsImpl userDetails, Long planId) {

        log.info("일정 id {}의 상세 정보 조회", planId);
        Plan plan = planReader.getPlan(planId);

        List<String> planImageUrl = imageReader.getImageList(planId, Image.EntityType.PLAN);
        List<UserListInfo> userList = getUserListFromAttendance(plan);
        int likeCount = likeRepository.countByPlan(plan);
        boolean isLiked = isPlanLikedByUser(userDetails, plan);
        MeetingInfoResponse meetingInfoResponse = getMeetingInfoResponse(plan);

        plan.updateViewCount();

        return PlanDetailResponse.fromEntity(plan, planImageUrl, plan.getMeeting(), userList.size(), likeCount, userList, meetingInfoResponse, isLiked);
    }

    /**
     * 일정 모집 취소
     *
     * @param email  사용자 이메일
     * @param planId 일정 id
     * @return 취소된 일정에 대한 메시지
     */
    @Override
    @Transactional
    public String cancelPlan(String email, Long planId) {

        User user = userReader.getUserByEmail(email);
        Plan plan = planReader.getPlan(planId);

        commUtilService.validateMeetingOwner(user, plan.getMeeting());
        plan.cancel();

        log.info("일정 id {}가 모집 취소되었습니다.", plan.getId());

        return "일정이 정상적으로 취소되었습니다.";
    }

    /**
     * 일정 참여 취소
     *
     * @param email  사용자 이메일
     * @param planId 일정 id
     * @return 참여 취소된 일정에 대한 메시지
     */
    @Override
    public String cancelAttendance(String email, Long planId) {

        User user = userReader.getUserByEmail(email);
        Plan plan = planReader.getPlan(planId);

        attendanceStore.quitPlan(user, plan);

        log.info("사용자 {}가 일정 id {}에서 나갔습니다.", user.getEmail(), plan.getId());

        return "일정 참여가 취소되었습니다.";
    }

    /**
     * 좋아요한 일정 목록 조회
     *
     * @param email      이메일
     * @param pageable   페이징 처리
     * @param query      검색어
     * @param province   시/도
     * @param district   군/구
     * @param startDate  시작 날짜
     * @param endDate    끝 날짜
     * @param categoryId 카테고리 id
     * @param sort       정렬 기준
     * @return 좋아요한 일정 목록
     */
    @Override
    @Transactional
    public UserPlanPagingResponse getLikedPlanList(String email, Pageable pageable, String query, String province, String district, String startDate, String endDate, Long categoryId, String sort) {
        // 사용자 검증
        userReader.getUserByEmail(email);
        return new UserPlanPagingResponse(planRepository.getLikedPlanList(email, pageable, query, province, district, startDate, endDate, categoryId, sort));
    }

    // 일정 참여자 목록을 가져오는 메서드
    private List<UserListInfo> getUserListFromAttendance(Plan plan) {

        List<Attendance> attendanceList = attendanceReader.getAttendanceList(plan);
        return attendanceList.stream()
                .map(UserListInfo::fromEntity)
                .collect(Collectors.toList());
    }

    // 사용자가 좋아요를 눌렀는지 확인
    private boolean isPlanLikedByUser(UserDetailsImpl userDetails, Plan plan) {

        if (userDetails == null || userDetails.isGuest()) {
            return false;
        }
        User user = userReader.getUserByEmail(userDetails.getUsername());
        return likeRepository.existsByUserAndPlan(user, plan);
    }

    // 일정에 대한 모임 정보 반환
    private MeetingInfoResponse getMeetingInfoResponse(Plan plan) {

        Meeting meeting = plan.getMeeting();
        String meetingImageUrl = imageReader.getMainImage(meeting.getId(), Image.EntityType.MEETING);
        List<UserListInfo> userListInfo = commUtilService.getUserListInfo(meeting);
        double reviewAverage = commUtilService.calculateReviewAverage(meeting);
        return MeetingInfoResponse.fromEntity(meeting, userListInfo.size(), reviewAverage, meetingImageUrl);
    }

}
