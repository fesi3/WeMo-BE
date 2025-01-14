package com.wemo.backend.domain.region.controller;

import com.wemo.backend.domain.region.dto.ProvinceListResponse;
import com.wemo.backend.domain.region.service.RegionService;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Regions")
@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @RequestMapping(value = "/province", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<ProvinceListResponse>> getProvinceList() {
        return ResponseEntity.ok(SuccessResponse.successWithData(regionService.getProvinceList()));
    }
}
