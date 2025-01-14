package com.wemo.backend.domain.region.controller;

import com.wemo.backend.domain.region.dto.DistrictListResponse;
import com.wemo.backend.domain.region.dto.ProvinceListResponse;
import com.wemo.backend.domain.region.service.RegionService;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Regions")
@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @Operation(summary = "전체 시/도 데이터 목록 조회", description = "전체 시/도 데이터 목록입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 시/도 데이터 목록 입니다.")
    })
    @RequestMapping(value = "/province", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<ProvinceListResponse>> getProvinceList() {
        return ResponseEntity.ok(SuccessResponse.successWithData(regionService.getProvinceList()));
    }

    @Operation(summary = "전체 군/구 데이터 목록 조회", description = "전체 군/구 데이터 목록입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 군/구 데이터 목록 입니다.")
    })
    @RequestMapping(value = "/district", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<DistrictListResponse>> getDistrictList(@RequestParam Long provinceId) {
        return ResponseEntity.ok(SuccessResponse.successWithData(regionService.getDistrictList(provinceId)));
    }

}
