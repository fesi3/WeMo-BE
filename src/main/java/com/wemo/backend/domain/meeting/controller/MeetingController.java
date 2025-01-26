package com.wemo.backend.domain.meeting.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.meeting.dto.*;
import com.wemo.backend.domain.meeting.service.MeetingService;
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

@Tag(name = "Meetings")
@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @Operation(summary = "모임 생성", description = "모임을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "모임이 생성되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<MeetingCreateResponse>> createMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                @Valid @RequestBody MeetingCreateRequest request) {

        return ResponseEntity.status(201).body(SuccessResponse.success(meetingService.createMeeting(userDetails.getUsername(), request), "모임 생성 성공"));
    }

    @Operation(summary = "모임 가입", description = "모임에 가입합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "모임에 가입되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 모임입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> joinMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long meetingId) {

        return ResponseEntity.status(201).body(SuccessResponse.successWithNoData(meetingService.joinMeeting(userDetails.getUsername(), meetingId)));
    }

    @Operation(summary = "모임 상세 조회", description = "모임 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청한 모임에 대한 상세 정보입니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 모임입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<MeetingDetailResponse>> getMeetingDetail(@PathVariable Long meetingId) {

        return ResponseEntity.ok(SuccessResponse.successWithData(meetingService.getMeetingDetail(meetingId)));
    }

    @Operation(summary = "모임 정보 수정", description = "모임 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임 정보가 수정되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 모임입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.PATCH)
    public ResponseEntity<SuccessResponse<String>> updateMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long meetingId,
                                                                 @Valid @RequestBody MeetingUpdateRequest request) {

        return ResponseEntity.ok(SuccessResponse.successWithNoData(meetingService.updateMeeting(userDetails.getUsername(), meetingId, request)));
    }

    @Operation(summary = "모임 삭제", description = "모임을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임이 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 모임입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.DELETE)
    public ResponseEntity<SuccessResponse<String>> deleteMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long meetingId) {

        return ResponseEntity.ok(SuccessResponse.successWithNoData(meetingService.deleteMeeting(userDetails.getUsername(), meetingId)));
    }

    @Operation(summary = "모임 멤버 목록 조회", description = "모임 멤버 목록입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임에 가입한 멤버의 목록입니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 모임입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{meetingId}/members", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<MeetingMemberPagingResponse>> getMemberListByMeeting(@PathVariable Long meetingId,
                                                                                               @RequestParam(defaultValue = "1") int page,
                                                                                               @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(SuccessResponse.successWithData(meetingService.getMemberListByMeeting(meetingId, pageable)));
    }


    @Operation(summary = "모임 일정 목록 조회", description = "모임의 전체 일정 목록입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임의 전체 일정 목록입니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 모임입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{meetingId}/plans", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<MeetingPlanPagingResponse>> getPlanListByMeeting(@PathVariable Long meetingId,
                                                                                           @RequestParam(defaultValue = "1") int page,
                                                                                           @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(SuccessResponse.successWithData(meetingService.getPlanListByMeeting(meetingId, pageable)));
    }

    @Operation(summary = "모임 후기 목록 조회", description = "모임의 전체 후기 목록입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임의 전체 후기 목록입니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 모임입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{meetingId}/reviews", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<MeetingReviewPagingResponse>> getReviewListByMeeting(@PathVariable Long meetingId,
                                                                                               @RequestParam(defaultValue = "1") int page,
                                                                                               @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(SuccessResponse.successWithData(meetingService.getReviewListByMeeting(meetingId, pageable)));
    }

    @Operation(summary = "모임 목록 조회", description = "회원 또는 비회원이 요청하는 모임의 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청한 모임 목록이 반환되었습니다.")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<MeetingCursorPagingResponse>> getMeetingList(@RequestParam(required = false) Long cursor,
                                                                                       @RequestParam(required = false, defaultValue = "10") int size,
                                                                                       @RequestParam(required = false) Long categoryId) {

        MeetingCursorPagingResponse response = meetingService.getMeetingList(cursor, size, categoryId);
        return ResponseEntity.ok(SuccessResponse.successWithData(response));
    }

}
