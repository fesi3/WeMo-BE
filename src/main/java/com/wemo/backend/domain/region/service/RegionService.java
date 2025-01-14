package com.wemo.backend.domain.region.service;

import com.wemo.backend.domain.region.dto.DistrictListResponse;
import com.wemo.backend.domain.region.dto.ProvinceListResponse;

public interface RegionService {

    ProvinceListResponse getProvinceList();

    DistrictListResponse getDistrictList(Long provinceId);

}
