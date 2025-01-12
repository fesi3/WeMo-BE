package com.wemo.backend.domain.meeting.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.meeting.dto.MeetingCreateRequest;
import com.wemo.backend.domain.meeting.dto.MeetingDetailResponse;
import com.wemo.backend.domain.meeting.service.MeetingService;
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

@Tag(name = "Meetings")
@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @Operation(summary = "모임 생성", description = "모임을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "모임이 생성되었습니다.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.")
    })
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> createMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @Valid @RequestBody MeetingCreateRequest request) {
        meetingService.createMeeting(userDetails.getUsername(), request);
        return ResponseEntity.status(201).body(SuccessResponse.successWithNoData("모임 생성 성공"));
    }

    @Operation(summary = "모임 가입", description = "모임에 가입합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "모임에 가입되었습니다.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당 모임이 존재하지 않습니다.")
    })
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> joinMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long meetingId) {
        return ResponseEntity.status(201).body(SuccessResponse.successWithNoData(meetingService.joinMeeting(userDetails.getUsername(), meetingId)));
    }

    @Operation(summary = "모임 상세 조회", description = "모임 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청한 모임에 대한 상세 정보입니다.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당 모임이 존재하지 않습니다.")
    })
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<MeetingDetailResponse>> getMeetingDetail(@PathVariable Long meetingId) {
        return ResponseEntity.ok(SuccessResponse.successWithData(meetingService.getMeetingDetail(meetingId)));
    }

}
