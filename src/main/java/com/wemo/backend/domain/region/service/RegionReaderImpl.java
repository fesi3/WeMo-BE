package com.wemo.backend.domain.region.service;

import com.wemo.backend.domain.region.entity.Province;
import com.wemo.backend.domain.region.repository.DistrictRepository;
import com.wemo.backend.domain.region.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RegionReaderImpl implements RegionReader {

    private final ProvinceRepository provinceRepository;

    private final DistrictRepository districtRepository;

    @Override
    public List<Province> getAllProvinceList() {

        return provinceRepository.findAll(Sort.by(Sort.Order.asc("id")));
    }

}
