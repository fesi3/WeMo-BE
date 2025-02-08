package com.wemo.backend.domain.region.service;

import com.wemo.backend.domain.region.entity.District;
import com.wemo.backend.domain.region.entity.Province;
import com.wemo.backend.domain.region.repository.DistrictRepository;
import com.wemo.backend.domain.region.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RegionStoreImpl implements RegionStore {

    private final ProvinceRepository provinceRepository;

    private final DistrictRepository districtRepository;

    @Override
    public District parseAndGetDistrict(String address) {

        Map<String, String> parsedAddress = RegionServiceImpl.parseAddress(address);
        Province province = findOrCreateProvince(parsedAddress.get("province"));
        return findOrCreateDistrict(parsedAddress.get("district"), province);
    }

    private Province findOrCreateProvince(String provinceName) {

        return provinceRepository.findByProvinceName(provinceName)
                .orElseGet(() -> provinceRepository.save(new Province(provinceName)));
    }

    private District findOrCreateDistrict(String districtName, Province province) {

        return districtRepository.findByDistrictNameAndProvince(districtName, province)
                .orElseGet(() -> districtRepository.save(new District(districtName, province)));
    }

}
