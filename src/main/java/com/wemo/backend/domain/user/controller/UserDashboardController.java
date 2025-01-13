package com.wemo.backend.domain.user.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.user.dto.UserMeetingPagingResponse;
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
    public ResponseEntity<SuccessResponse<UserMeetingPagingResponse>> getMyMeetingList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                       @RequestParam(defaultValue = "1") int page,
                                                                                       @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(SuccessResponse.successWithData(userService.getMyMeetingList(userDetails.getUsername(), pageable)));
    }

}
