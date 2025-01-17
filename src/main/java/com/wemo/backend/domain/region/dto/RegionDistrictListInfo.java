package com.wemo.backend.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegionDistrictListInfo {

    private Long id;

    private String name;

}
