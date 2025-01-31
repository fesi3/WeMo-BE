package com.wemo.backend.domain.user.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.user.dto.UserMeetingPagingResponse;
import com.wemo.backend.domain.user.dto.UserPlanPagingResponse;
import com.wemo.backend.domain.user.dto.UserReviewPagingResponse;
import com.wemo.backend.domain.user.service.UserService;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserDashboardController {

    private final UserService userService;

    @Operation(summary = "내 모임 목록 조회", description = "유저가 속한 모임의 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 속한 모임 목록이 반환되었습니다.")
    })
    @RequestMapping(value = "/meetings", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<UserMeetingPagingResponse>> getMeetingList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                     @RequestParam(defaultValue = "1") int page,
                                                                                     @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(SuccessResponse.successWithData(userService.getMeetingList(userDetails.getUsername(), pageable)));
    }

    @Operation(summary = "내가 만든 모임 목록 조회", description = "유저가 만든 모임의 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 만든 모임 목록이 반환되었습니다.")
    })
    @RequestMapping(value = "/meetings/me", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<UserMeetingPagingResponse>> getMyMeetingList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                       @RequestParam(defaultValue = "1") int page,
                                                                                       @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(SuccessResponse.successWithData(userService.getMyMeetingList(userDetails.getUsername(), pageable)));
    }

    @Operation(summary = "내 일정 목록 조회", description = "유저가 참여한 일정의 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 참여한 일정의 목록이 반환되었습니다.")
    })
    @RequestMapping(value = "/plans", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<UserPlanPagingResponse>> getPlanList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                               @RequestParam(defaultValue = "1") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        return ResponseEntity.ok(SuccessResponse.successWithData(userService.getPlanList(userDetails.getUsername(), pageable)));

    }

    @Operation(summary = "내가 만든 일정 목록 조회", description = "유저가 만든 일정의 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 만든 일정의 목록이 반환되었습니다.")
    })
    @RequestMapping(value = "/plans/me", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<UserPlanPagingResponse>> getMyPlanList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                 @RequestParam(defaultValue = "1") int page,
                                                                                 @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        return ResponseEntity.ok(SuccessResponse.successWithData(userService.getMyPlanList(userDetails.getUsername(), pageable)));

    }

    @Operation(summary = "내가 작성한 후기 목록 조회", description = "유저가 작성한 후기의 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 작성한 후기의 목록이 반환되었습니다.")
    })
    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<UserReviewPagingResponse>> getMyReviewList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                     @RequestParam(defaultValue = "1") int page,
                                                                                     @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        return ResponseEntity.ok(SuccessResponse.successWithData(userService.getMyReviewList(userDetails.getUsername(), pageable)));

    }

    @Operation(summary = "후기 작성 가능한 일정 목록 조회", description = "후기 작성 가능한 일정의 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "후기 작성 가능한 일정의 목록이 반환되었습니다.")
    })
    @RequestMapping(value = "/reviews/available", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<UserPlanPagingResponse>> getPlanListReviewAvailable(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                              @RequestParam(defaultValue = "1") int page,
                                                                                              @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        return ResponseEntity.ok(SuccessResponse.successWithData(userService.getPlanListReviewAvailable(userDetails.getUsername(), pageable)));

    }

}
