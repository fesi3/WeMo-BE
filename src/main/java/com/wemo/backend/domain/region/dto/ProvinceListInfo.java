package com.wemo.backend.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProvinceListInfo {

    private Long provinceId;

    private String name;

}
