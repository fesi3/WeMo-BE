package com.wemo.backend.domain.plan.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.plan.dto.PlanCreateRequest;
import com.wemo.backend.domain.plan.dto.PlanCreateResponse;
import com.wemo.backend.domain.plan.service.PlanService;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Plans")
@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @Operation(summary = "일정 생성", description = "일정을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "일정이 생성되었습니다.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.")
    })
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<PlanCreateResponse>> createPlan(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                          @Valid @RequestBody PlanCreateRequest request,
                                                                          @PathVariable Long meetingId) {

        return ResponseEntity.status(201).body(SuccessResponse.successWithData(planService.createPlan(userDetails.getUsername(), request, meetingId)));
    }

    @Operation(summary = "일정 참여", description = "일정에 참여합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "일정에 참여되었습니다.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당 모임이 존재하지 않습니다.")
    })
    @RequestMapping(value = "/{planId}/attendance", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> joinPlan(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long planId) {
        return ResponseEntity.status(201).body(SuccessResponse.successWithNoData(planService.joinPlan(userDetails.getUsername(), planId)));
    }

}
