package com.wemo.backend.domain.meeting.service;

import com.wemo.backend.domain.comm.CommUtilService;
import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.image.service.ImageReader;
import com.wemo.backend.domain.image.service.ImageStore;
import com.wemo.backend.domain.meeting.dto.*;
import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meeting.repository.MeetingRepository;
import com.wemo.backend.domain.meetingMember.service.MeetingMemberStore;
import com.wemo.backend.domain.plan.dto.PlanListInfo;
import com.wemo.backend.domain.review.dto.ReviewListInfo;
import com.wemo.backend.domain.user.dto.UserListInfo;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final UserReader userReader;
    private final MeetingStore meetingStore;
    private final MeetingMemberStore meetingMemberStore;
    private final ImageStore imageStore;
    private final MeetingReader meetingReader;
    private final ImageReader imageReader;
    private final MeetingRepository meetingRepository;
    private final CommUtilService commUtilService;
    private final MeetingUtilService meetingUtilService;

    /**
     * 모임 생성
     * - 사용자 인증을 통해 요청한 사용자가 유효한지 확인하고, 모임 저장
     * - 모임에 대한 이미지 파일 저장
     * - 모임 주최자는 자동으로 가입되도록 설정
     *
     * @param email   사용자의 이메일
     * @param request 모임 생성 요청 객체
     */
    @Override
    @Transactional
    public MeetingCreateResponse createMeeting(String email, MeetingCreateRequest request) {

        User user = validateUser(email);
        Meeting meeting = meetingStore.storeMeeting(request, user);
        List<String> meetingImagePath = imageStore.storeImageList(user, meeting.getId(), request.getFileUrls(), Image.EntityType.MEETING);
        meetingMemberStore.storeMemberToMeeting(user, meeting);
        log.info("사용자 {}의 모임 id {}가 생성되었습니다.", user.getEmail(), meeting.getId());

        return MeetingCreateResponse.fromEntity(meeting, meetingImagePath);

    }

    /**
     * 모임 가입
     * - 사용자 인증 후 모임에 참여할 수 있도록 설정
     *
     * @param email     사용자의 이메일
     * @param meetingId 모임 id
     * @return 모임 가입 성공 메시지
     */
    @Override
    @Transactional
    public String joinMeeting(String email, Long meetingId) {

        User user = validateUser(email);
        Meeting meeting = validateMeeting(meetingId);
        meetingMemberStore.storeMemberToMeeting(user, meeting);
        log.info("사용자 {}가 모임 id {}에 가입했습니다.", user.getEmail(), meeting.getId());
        return "모임 가입 성공";
    }

    /**
     * 모임 상세 정보 조회
     * - 모임에 대한 이미지, 멤버, 일정, 후기 정보를 포함하여 반환
     * - 후기는 전체 일정의 평균 평점을 포함
     *
     * @param meetingId 모임 id
     * @return 모임 상세 정보 응답 객체
     */
    @Override
    public MeetingDetailResponse getMeetingDetail(Long meetingId) {

        log.info("모임 id {} 상세 조회 요청", meetingId);
        Meeting meeting = validateMeeting(meetingId);

        List<String> meetingImage = imageReader.getImageList(meeting.getId(), Image.EntityType.MEETING);
        List<UserListInfo> userListInfoList = meetingUtilService.getTopUsers(meeting);
        List<PlanListInfo> planListInfoList = meetingUtilService.getTopPlans(meeting);
        List<ReviewListInfo> reviewListInfoList = meetingUtilService.getTopReviews(planListInfoList);

        double reviewAverage = commUtilService.calculateReviewAverage(meeting);

        return MeetingDetailResponse.fromEntity(
                meeting,
                meetingImage,
                userListInfoList.size(),
                userListInfoList,
                planListInfoList.size(),
                planListInfoList,
                reviewListInfoList.size(),
                reviewAverage,
                reviewListInfoList
        );
    }

    /**
     * 모임 수정
     * - 모임의 설명을 수정할 수 있으며, 모임의 소유자만 수정 가능
     *
     * @param email     사용자의 이메일
     * @param meetingId 모임 id
     * @param request   모임 수정 요청 객체
     * @return 모임 수정 성공 메시지
     */
    @Override
    @Transactional
    public String updateMeeting(String email, Long meetingId, MeetingUpdateRequest request) {

        User user = validateUser(email);
        Meeting meeting = validateMeeting(meetingId);
        commUtilService.validateMeetingOwner(user, meeting);
        meeting.updateDescription(request.getDescription());
        log.info("사용자 {}의 모임 id {}가 수정되었습니다.", user.getEmail(), meeting.getId());
        return "모임 내용이 수정되었습니다.";
    }

    /**
     * 모임 삭제
     * - 모임과 관련된 모든 데이터(일정, 후기, 멤버 등) 삭제
     *
     * @param email     사용자의 이메일
     * @param meetingId 모임 id
     * @return 모임 삭제 성공 메시지
     */
    @Override
    @Transactional
    public String deleteMeeting(String email, Long meetingId) {

        log.info("모임 id {}의 삭제 요청", meetingId);

        User user = validateUser(email);
        Meeting meeting = validateMeeting(meetingId);
        commUtilService.validateMeetingOwner(user, meeting);

        // 모임 소프트 삭제
        meeting.softDelete();

        // 관련 데이터 삭제
        deleteAssociatedData(meeting);

        log.info("사용자 {}가 모임 id {}를 정상적으로 삭제했습니다.", user.getEmail(), meeting.getId());
        return "모임이 삭제되었습니다.";
    }

    /**
     * 모임 멤버 목록 조회
     * - 페이징 처리된 멤버 목록 반환
     *
     * @param meetingId 모임 id
     * @param pageable  페이지네이션 정보
     * @return 모임 멤버 페이징 응답 객체
     */
    @Override
    public MeetingMemberPagingResponse getMemberListByMeeting(Long meetingId, Pageable pageable) {

        Meeting meeting = validateMeeting(meetingId);
        return new MeetingMemberPagingResponse(meeting, meetingRepository.getMemberListByMeeting(meeting, pageable));
    }

    /**
     * 모임 일정 목록 조회
     * - 페이징 처리된 일정 목록 반환
     *
     * @param meetingId 모임 id
     * @param pageable  페이지네이션 정보
     * @return 모임 일정 페이징 응답 객체
     */
    @Override
    public MeetingPlanPagingResponse getPlanListByMeeting(Long meetingId, Pageable pageable) {

        Meeting meeting = validateMeeting(meetingId);
        return new MeetingPlanPagingResponse(meeting, meetingRepository.getPlanListByMeeting(meeting, pageable));
    }

    /**
     * 모임 후기 목록 조회
     * - 페이징 처리된 후기 목록 반환
     *
     * @param meetingId 모임 id
     * @param pageable  페이지네이션 정보
     * @return 모임 후기 페이징 응답 객체
     */
    @Override
    public MeetingReviewPagingResponse getReviewListByMeeting(Long meetingId, Pageable pageable) {

        Meeting meeting = validateMeeting(meetingId);
        return new MeetingReviewPagingResponse(meeting, meetingRepository.getReviewListByMeeting(meeting, pageable));
    }

    /**
     * 모임 목록 조회
     * - 커서 기반 페이징 처리된 모임 목록 반환
     *
     * @param cursor     커서 id
     * @param size       데이터 개수
     * @param categoryId 카테고리 id
     * @return 요청 조건에 알맞은 모임 목록
     */
    @Override
    public MeetingCursorPagingResponse getMeetingList(Long cursor, int size, Long categoryId) {

        return meetingRepository.getMeetingList(cursor, size, categoryId);
    }

    /**
     * 모임 가입 취소
     *
     * @param email     사용자 이메일
     * @param meetingId 모임 id
     * @return 모임 가입 취소 성공 메세지
     */
    @Override
    public String joinCancelMeeting(String email, Long meetingId) {

        User user = userReader.getUserByEmail(email);
        Meeting meeting = meetingReader.getMeeting(meetingId);

        meetingMemberStore.deleteMember(user, meeting);
        return "모임 가입 취소 완료";
    }

    // 유틸리티 메서드들

    /**
     * 사용자 이메일을 통해 사용자를 검증
     *
     * @param email 사용자 이메일
     * @return 검증된 사용자 객체
     */
    private User validateUser(String email) {

        return userReader.getUserByEmail(email);
    }

    /**
     * 모임 id를 통해 모임을 검증
     *
     * @param meetingId 모임 id
     * @return 검증된 모임 객체
     */
    private Meeting validateMeeting(Long meetingId) {

        return meetingReader.getMeeting(meetingId);
    }

    /**
     * 모임에 대한 모든 연관 데이터를 삭제
     * - 일정, 후기, 이미지, 멤버 데이터 삭제
     *
     * @param meeting 모임 객체
     */
    private void deleteAssociatedData(Meeting meeting) {

        meetingUtilService.deletePlansAndReviews(meeting);
        meetingUtilService.deleteMembers(meeting);
        meetingUtilService.deleteMeetingImages(meeting);
    }

}
