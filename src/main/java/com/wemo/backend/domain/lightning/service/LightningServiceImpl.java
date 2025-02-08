package com.wemo.backend.domain.lightning.service;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.lightning.dto.LightningCursorPagingResponse;
import com.wemo.backend.domain.lightning.dto.LightningDetailResponse;
import com.wemo.backend.domain.lightning.dto.LightningRequest;
import com.wemo.backend.domain.lightning.dto.LightningResponse;
import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightning.entity.LightningType;
import com.wemo.backend.domain.lightning.repository.LightningRepository;
import com.wemo.backend.domain.lightningJoin.repository.LightningJoinRepository;
import com.wemo.backend.domain.lightningJoin.service.LightningJoinStore;
import com.wemo.backend.domain.region.entity.District;
import com.wemo.backend.domain.region.service.RegionStore;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LightningServiceImpl implements LightningService {

    private final UserReader userReader;

    private final LightningTypeReader lightningTypeReader;

    private final RegionStore regionStore;

    private final LightningStore lightningStore;

    private final LightningJoinStore lightningJoinStore;

    private final LightningRepository lightningRepository;

    private final LightningReader lightningReader;

    private final LightningJoinRepository lightningJoinRepository;

    /**
     * 번개 모임 생성
     *
     * @param email   사용자 이메일
     * @param request 번개 모임 생성 데이터
     * @return 생성된 번개 모임 정보
     */
    @Override
    @Transactional
    public LightningResponse createLightnings(String email, LightningRequest request) {

        User user = userReader.getActiveUserByEmail(email);
        LightningType lightningType = lightningTypeReader.getLightningType(request.getLightningTypeId());
        District district = regionStore.parseAndGetDistrict(request.getAddress());
        Lightning lightning = lightningStore.store(user, lightningType, district, request);

        log.info("번개 모임 생성 - 모임 ID: {}, 사용자: {}", lightning.getId(), email);
        // 주최자는 자동 참여
        lightningJoinStore.store(user, lightning);
        log.info("번개 모임 자동 가입 - 모임 ID: {}, 사용자: {}", lightning.getId(), email);

        return LightningResponse.fromEntity(lightning);
    }

    /**
     * 번개모임 목록 조회
     *
     * @param cursor          커서 id
     * @param size            조회 요청 개수
     * @param province        시/도
     * @param district        군/구
     * @param lightningTypeId 번개 모임 종류 id
     * @param lightningTimeId 번개 모임 시간 타입 id
     * @param latitude        위도
     * @param longitude       경도
     * @param radius          반경
     * @return 조건에 맞는 데이터 목록
     */
    @Override
    public LightningCursorPagingResponse getLightningMeetingList(Long cursor, int size, String province, String district, Long lightningTypeId, Integer lightningTimeId, Double latitude, Double longitude, Double radius) {

        return lightningRepository.getLightningMeetingList(cursor, size, province, district, lightningTypeId, lightningTimeId, latitude, longitude, radius);
    }

    /**
     * 번개 모임 상세 조회
     *
     * @param userDetails 유저 정보 객체
     * @param lightningId 번개 모임 id
     * @return 요청한 번개 모임 상세 정보
     */
    @Override
    public LightningDetailResponse getLightningMeetingDetail(UserDetailsImpl userDetails, Long lightningId) {

        log.info("번개 모임 상세 조회 - 모임 ID: {}", lightningId);
        // 번개 모임 유효성 검사 및 조회
        Lightning lightning = lightningReader.getLightningById(lightningId);

        // 회원인 경우 참여 내역 확인
        if (userDetails != null && !userDetails.isGuest()) {
            User user = userReader.getActiveUserByEmail(userDetails.getUsername());
            boolean isJoined = lightningJoinRepository.existsByUserAndLightning(user, lightning);
            return LightningDetailResponse.fromEntity(lightning, isJoined);
        }

        // 비회원인 경우 참여 내역 false
        return LightningDetailResponse.fromEntity(lightning, false);
    }

    /**
     * 번개 모임 수정
     *
     * @param email       사용자 이메일
     * @param lightningId 번개 모임 id
     * @param request     수정 요청 데이터
     * @return 수정된 번개 모임 데이터
     */
    @Override
    @Transactional
    public LightningResponse updateLightnings(String email, Long lightningId, LightningRequest request) {

        // 유저 및 모임 권한 검증
        User user = userReader.getActiveUserByEmail(email);
        Lightning lightning = lightningReader.validateLightningOwnership(user, lightningId);

        LightningType lightningType = lightningTypeReader.getLightningType(request.getLightningTypeId());
        District district = regionStore.parseAndGetDistrict(request.getAddress());

        lightning.update(request, lightningType, district);

        log.info("번개 모임 수정 - 모임 ID: {}, 사용자: {}", lightning.getId(), email);

        return LightningResponse.fromEntity(lightning);
    }

    /**
     * 번개 모임 삭제
     *
     * @param email       사용자 이메일
     * @param lightningId 번개 모임 id
     * @return 응답 메세지
     */
    @Override
    @Transactional
    public String deleteLightningMeeting(String email, Long lightningId) {

        // 유저 및 모임 권한 검증
        User user = userReader.getActiveUserByEmail(email);
        Lightning lightning = lightningReader.validateLightningOwnership(user, lightningId);

        lightningJoinRepository.deleteAllByLightning(lightning);
        lightningStore.delete(lightning);

        log.info("번개 모임 삭제 - 모임 ID: {}, 사용자: {}", lightning.getId(), email);

        return "번개 모임이 성공적으로 삭제되었습니다.";
    }

}
