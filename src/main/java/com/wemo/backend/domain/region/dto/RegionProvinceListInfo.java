package com.wemo.backend.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RegionProvinceListInfo {

    private Long id;

    private String name;

    private List<RegionDistrictListInfo> districtList;

}
