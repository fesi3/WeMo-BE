package com.wemo.backend.domain.region.repository;

import com.wemo.backend.domain.region.entity.District;
import com.wemo.backend.domain.region.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Long> {

    Optional<District> findByDistrictNameAndProvince(String districtName, Province province);

    List<District> findAllByProvince(Province province);

}
