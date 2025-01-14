package com.wemo.backend.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DistrictListInfo {

    private Long provinceId;

    private Long districtId;

    private String name;

}
