package com.wemo.backend.domain.region.service;

import com.wemo.backend.domain.region.entity.District;
import com.wemo.backend.domain.region.entity.Province;
import com.wemo.backend.domain.region.repository.DistrictRepository;
import com.wemo.backend.domain.region.repository.ProvinceRepository;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wemo.backend.global.exception.ErrorCode.PROVINCE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class RegionReaderImpl implements RegionReader {

    private final ProvinceRepository provinceRepository;

    private final DistrictRepository districtRepository;

    @Override
    public List<Province> getAllProvinceList() {

        return provinceRepository.findAll(Sort.by(Sort.Order.asc("id")));
    }

    @Override
    public List<District> getAllDistrictList(Long provinceId) {

        Province province = provinceRepository.findById(provinceId).orElseThrow(
                () -> new CustomException(PROVINCE_NOT_FOUND)
        );

        return districtRepository.findAllByProvince(province);
    }

}
