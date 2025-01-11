package com.wemo.backend.domain.meeting.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.meeting.dto.MeetingCreateRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}
