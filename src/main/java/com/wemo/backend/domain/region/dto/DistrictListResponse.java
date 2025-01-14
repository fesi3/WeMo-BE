package com.wemo.backend.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DistrictListResponse {

    List<DistrictListInfo> districtList;
}
