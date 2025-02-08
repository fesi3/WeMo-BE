package com.wemo.backend.domain.lightning.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.lightning.dto.LightningCursorPagingResponse;
import com.wemo.backend.domain.lightning.dto.LightningDetailResponse;
import com.wemo.backend.domain.lightning.dto.LightningRequest;
import com.wemo.backend.domain.lightning.dto.LightningResponse;
import com.wemo.backend.domain.lightning.service.LightningService;
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

@Tag(name = "Lightnings")
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
    public ResponseEntity<SuccessResponse<LightningResponse>> createLightnings(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                               @Valid @RequestBody LightningRequest request) {

        return ResponseEntity.status(201).body(SuccessResponse.successWithData(lightningService.createLightnings(userDetails.getUsername(), request)));
    }

    @Operation(summary = "번개 모임 목록 조회", description = "번개 모임 일정의 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "요청한 일정 목록이 반환되었습니다.")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<LightningCursorPagingResponse>> getLightningMeetingList(@RequestParam(required = false) Long cursor,
                                                                                                  @RequestParam(required = false, defaultValue = "10") int size,
                                                                                                  @RequestParam(required = false) String province,
                                                                                                  @RequestParam(required = false) String district,
                                                                                                  @RequestParam(required = false) Long lightningTypeId,
                                                                                                  @RequestParam(required = false) Integer lightningTimeId,
                                                                                                  @RequestParam(required = false) Double latitude,
                                                                                                  @RequestParam(required = false) Double longitude,
                                                                                                  @RequestParam(required = false, defaultValue = "1.0") Double radius) {

        return ResponseEntity.ok(SuccessResponse.successWithData(lightningService.getLightningMeetingList(cursor, size, province, district,
                lightningTypeId, lightningTimeId, latitude, longitude, radius)));
    }

    @Operation(summary = "번개 모임 상세조회", description = "요청한 번개 모임의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청한 번개 모임의 상세 정보가 반환되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 모임입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{lightningId}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<LightningDetailResponse>> getLightningMeetingDetail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                              @PathVariable Long lightningId) {

        return ResponseEntity.ok(SuccessResponse.successWithData(lightningService.getLightningMeetingDetail(userDetails, lightningId)));
    }

    @Operation(summary = "번개 모임 수정", description = "번개 모임 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "번개 모임 정보가 수정되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{lightningId}", method = RequestMethod.PUT)
    public ResponseEntity<SuccessResponse<LightningResponse>> updateLightnings(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                               @PathVariable Long lightningId,
                                                                               @Valid @RequestBody LightningRequest request) {

        return ResponseEntity.ok(SuccessResponse.successWithData(lightningService.updateLightnings(userDetails.getUsername(), lightningId, request)));
    }

    @Operation(summary = "번개 모임 삭제", description = "번개 모임을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "번개 모임이 삭제되었습니다.")
    @RequestMapping(value = "/{lightningId}", method = RequestMethod.DELETE)
    public ResponseEntity<SuccessResponse<String>> deleteLightningMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                          @PathVariable Long lightningId) {

        return ResponseEntity.ok(SuccessResponse.successWithNoData(lightningService.deleteLightningMeeting(userDetails.getUsername(), lightningId)));
    }

}

