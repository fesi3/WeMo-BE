package com.wemo.backend.domain.plan.controller;

import com.amazonaws.Response;
import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.plan.dto.PlanCreateRequest;
import com.wemo.backend.domain.plan.dto.PlanCreateResponse;
import com.wemo.backend.domain.plan.dto.PlanCursorPagingResponse;
import com.wemo.backend.domain.plan.service.PlanService;
import com.wemo.backend.domain.plan.dto.PlanDetailResponse;
import com.wemo.backend.domain.user.dto.UserPlanPagingResponse;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            @ApiResponse(responseCode = "201", description = "일정이 생성되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<PlanCreateResponse>> createPlan(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                          @Valid @RequestBody PlanCreateRequest request,
                                                                          @PathVariable Long meetingId) {

        return ResponseEntity.status(201).body(SuccessResponse.successWithData(planService.createPlan(userDetails.getUsername(), request, meetingId)));
    }

    @Operation(summary = "일정 참여", description = "일정에 참여합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "일정에 참여되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일정입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{planId}/attendance", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> joinPlan(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long planId) {
        return ResponseEntity.status(201).body(SuccessResponse.successWithNoData(planService.joinPlan(userDetails.getUsername(), planId)));
    }

    @Operation(summary = "일정 목록 조회", description = "회원 또는 비회원이 요청하는 일정의 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청한 일정 목록이 반환되었습니다.")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<PlanCursorPagingResponse>> getGatherings(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                   @RequestParam(required = false) Long cursor, // 커서 파라미터 추가
                                                                                   @RequestParam int size,
                                                                                   @RequestParam(required = false) String query,
                                                                                   @RequestParam(required = false) String province,
                                                                                   @RequestParam(required = false) String district,
                                                                                   @RequestParam(required = false) String startDate,
                                                                                   @RequestParam(required = false) String endDate,
                                                                                   @RequestParam(required = false) Long categoryId,
                                                                                   @RequestParam(required = false) String sort) {

        PlanCursorPagingResponse response = planService.getPlanList(userDetails, cursor, size, query, province, district, startDate, endDate, categoryId, sort);
        return ResponseEntity.ok(SuccessResponse.successWithData(response));
    }

    @Operation(summary = "일정 상세 조회", description = "회원 또는 비회원이 요청한 일정의 상세 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청한 일정의 상세 내용이 반환되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일정입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{planId}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<PlanDetailResponse>> getPlanDetail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                             @PathVariable Long planId) {

        return ResponseEntity.ok(SuccessResponse.successWithData(planService.getPlanDetail(userDetails, planId)));
    }

    @Operation(summary = "일정 모집 취소", description = "일정이 취소되었습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정이 취소되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일정입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{planId}/cancel", method = RequestMethod.PATCH)
    public ResponseEntity<SuccessResponse<String>> cancelPlan(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable Long planId) {

        return ResponseEntity.ok(SuccessResponse.successWithNoData(planService.cancelPlan(userDetails.getUsername(), planId)));
    }

    @Operation(summary = "일정 참여 취소", description = "일정 참여를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정 참여 취소되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일정입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{planId}/attendance", method = RequestMethod.DELETE)
    public ResponseEntity<SuccessResponse<String>> cancelAttendance(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long planId) {
        return ResponseEntity.status(201).body(SuccessResponse.successWithNoData(planService.cancelAttendance(userDetails.getUsername(), planId)));
    }

    @Operation(summary = "좋아요한 일정 목록 조회", description = "유저가 좋아요한 일정의 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요한 일정의 목록이 반환되었습니다.")
    })
    @RequestMapping(value = "/like", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<UserPlanPagingResponse>> getLikedPlanList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                 @RequestParam(defaultValue = "1") int page,
                                                                                 @RequestParam(defaultValue = "10") int size,
                                                                                 @RequestParam(required = false) String query,
                                                                                 @RequestParam(required = false) String province,
                                                                                 @RequestParam(required = false) String district,
                                                                                 @RequestParam(required = false) String startDate,
                                                                                 @RequestParam(required = false) String endDate,
                                                                                 @RequestParam(required = false) Long categoryId,
                                                                                 @RequestParam(required = false) String sort) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        return ResponseEntity.ok(SuccessResponse.successWithData(planService.getLikedPlanList(userDetails.getUsername(), pageable, query, province, district, startDate, endDate, categoryId, sort)));

    }

}
