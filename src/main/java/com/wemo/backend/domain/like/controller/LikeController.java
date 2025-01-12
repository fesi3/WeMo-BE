package com.wemo.backend.domain.like.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.like.service.LikeService;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Plans")
@RestController
@RequestMapping("/api/plans/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "일정 좋아요", description = "일정에 좋아요를 누릅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "일정에 좋아요를 눌렀습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일정입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{planId}", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> likePlan(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long planId) {
        return ResponseEntity.status(201).body(SuccessResponse.successWithNoData(likeService.likePlan(userDetails.getUsername(), planId)));
    }

    @Operation(summary = "일정 좋아요 취소", description = "일정에 누른 좋아요를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정에 누른 좋아요를 취소했습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일정입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{planId}", method = RequestMethod.DELETE)
    public ResponseEntity<SuccessResponse<String>> deleteLikePlan(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PathVariable Long planId) {
        return ResponseEntity.ok(SuccessResponse.successWithNoData(likeService.deleteLikePlan(userDetails.getUsername(), planId)));
    }
}
