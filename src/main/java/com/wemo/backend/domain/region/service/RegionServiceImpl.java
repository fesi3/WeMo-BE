package com.wemo.backend.domain.region.service;

import com.wemo.backend.domain.region.dto.*;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wemo.backend.global.exception.ErrorCode.INVALID_ADDRESS;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionReader regionReader;

    public static Map<String, String> parseAddress(String address) {

        String[] parts = address.split(" ");
        if (parts.length < 2) {
            throw new CustomException(INVALID_ADDRESS);
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

    @Override
    public RegionListResponse getRegionList() {

        // 전체 시/도 목록을 가져옵니다.
        List<RegionProvinceListInfo> provinceList = regionReader.getAllProvinceList().stream()
                .map(province -> {
                    // 각 시/도에 대해 군/구 목록을 가져옵니다.
                    List<RegionDistrictListInfo> districtList = regionReader.getAllDistrictList(province.getId()).stream()
                            .map(district -> RegionDistrictListInfo.builder()
                                    .id(district.getId())
                                    .name(district.getDistrictName())
                                    .build())
                            .toList();

                    // 시/도와 해당 군/구 목록을 포함한 객체 생성
                    return RegionProvinceListInfo.builder()
                            .id(province.getId())
                            .name(province.getProvinceName())
                            .districtList(districtList)
                            .build();
                })
                .toList();

        // 변환된 리스트로 RegionListResponse 생성
        return new RegionListResponse(provinceList);
    }

}
