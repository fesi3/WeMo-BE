package com.wemo.backend.domain.region.service;

import com.wemo.backend.domain.region.dto.DistrictListInfo;
import com.wemo.backend.domain.region.dto.DistrictListResponse;
import com.wemo.backend.domain.region.dto.ProvinceListInfo;
import com.wemo.backend.domain.region.dto.ProvinceListResponse;
import com.wemo.backend.domain.region.entity.District;
import com.wemo.backend.domain.region.entity.Province;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wemo.backend.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionReader regionReader;

    public static Map<String, String> parseAddress(String address) {

        String[] parts = address.split(" ");
        if (parts.length < 2) {
            throw new CustomException(ILLEGAL_ADDRESS_NOT_VALID);
        }

        Map<String, String> addressMap = new HashMap<>();
        addressMap.put("province", parts[0]); // 시/도
        addressMap.put("district", parts[1]); // 군/구
        return addressMap;
    }

    /**
     * 전체 시/도 데이터 목록 조회
     *
     * @return 전체 시/도 데이터 목록
     */
    @Override
    public ProvinceListResponse getProvinceList() {

        // Province 객체를 ProvinceListInfo로 변환하여 리스트로 수집
        List<ProvinceListInfo> provinceListInfos = regionReader.getAllProvinceList().stream()
                .map(province -> ProvinceListInfo.builder()
                        .provinceId(province.getId())
                        .name(province.getProvinceName())
                        .build())
                .toList();

        // 변환된 리스트로 ProvinceListResponse 생성
        return new ProvinceListResponse(provinceListInfos);
    }

    @Override
    public DistrictListResponse getDistrictList(Long provinceId) {

        // District 객체를 DistrictListInfo로 변환하여 리스트로 수집
        List<DistrictListInfo> districtListInfos = regionReader.getAllDistrictList(provinceId).stream()
                .map(district -> DistrictListInfo.builder()
                        .provinceId(provinceId)
                        .districtId(district.getId())
                        .name(district.getDistrictName())
                        .build())
                .toList();

        return new DistrictListResponse(districtListInfos);
    }


}
