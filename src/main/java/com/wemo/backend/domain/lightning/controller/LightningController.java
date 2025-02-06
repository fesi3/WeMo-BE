package com.wemo.backend.domain.lightning.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.lightning.dto.LightningCreateRequest;
import com.wemo.backend.domain.lightning.dto.LightningCreateResponse;
import com.wemo.backend.domain.lightning.service.LightningService;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lightnings")
public class LightningController {

    private final LightningService lightningService;

    @Operation(summary = "번개 모임 생성", description = "번개 모임을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "번개 모임이 생성되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<LightningCreateResponse>> createLightnings(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                     @Valid @RequestBody LightningCreateRequest request) {

        return ResponseEntity.status(201).body(SuccessResponse.successWithData(lightningService.createLightnings(userDetails.getUsername(), request)));
    }

}

