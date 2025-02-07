package com.wemo.backend.domain.lightning.service;

import com.wemo.backend.domain.lightning.dto.LightningCreateRequest;
import com.wemo.backend.domain.lightning.dto.LightningCreateResponse;
import com.wemo.backend.domain.lightning.dto.LightningCursorPagingResponse;
import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightning.entity.LightningType;
import com.wemo.backend.domain.lightning.repository.LightningRepository;
import com.wemo.backend.domain.lightningJoin.service.LightningJoinStore;
import com.wemo.backend.domain.region.entity.District;
import com.wemo.backend.domain.region.entity.Province;
import com.wemo.backend.domain.region.repository.DistrictRepository;
import com.wemo.backend.domain.region.repository.ProvinceRepository;
import com.wemo.backend.domain.region.service.RegionServiceImpl;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LightningServiceImpl implements LightningService {

    private final UserReader userReader;

    private final LightningTypeReader lightningTypeReader;

    private final ProvinceRepository provinceRepository;

    private final DistrictRepository districtRepository;

    private final LightningStore lightningStore;

    private final LightningJoinStore lightningJoinStore;

    private final LightningRepository lightningRepository;

    /**
     * 번개 모임 생성
     *
     * @param email   사용자 이메일
     * @param request 번개 모임 생성 데이터
     * @return 생성된 번개 모임 정보
     */
    @Override
    @Transactional
    public LightningCreateResponse createLightnings(String email, LightningCreateRequest request) {

        User user = userReader.getUserByEmail(email);
        LightningType lightningType = lightningTypeReader.getLightningType(request.getLightningTypeId());

        Map<String, String> parsedAddress = RegionServiceImpl.parseAddress(request.getAddress());
        Province province = getOrSaveProvince(parsedAddress.get("province"));
        District district = getOrSaveDistrict(parsedAddress.get("district"), province);

        Lightning lightning = lightningStore.store(user, lightningType, district, request);

        // 주최자는 자동 참여
        lightningJoinStore.store(user, lightning);

        return LightningCreateResponse.fromEntity(lightning);
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

    private Province getOrSaveProvince(String provinceName) {

        return provinceRepository.findByProvinceName(provinceName)
                .orElseGet(() -> provinceRepository.save(new Province(provinceName)));
    }

    private District getOrSaveDistrict(String districtName, Province province) {

        return districtRepository.findByDistrictNameAndProvince(districtName, province)
                .orElseGet(() -> districtRepository.save(new District(districtName, province)));
    }


}
