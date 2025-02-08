package com.wemo.backend.domain.region.service;

import com.wemo.backend.domain.region.entity.District;

public interface RegionStore {

    District parseAndGetDistrict(String address);

}
