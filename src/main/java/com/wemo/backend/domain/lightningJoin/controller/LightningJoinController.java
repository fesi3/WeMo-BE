package com.wemo.backend.domain.lightningJoin.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.lightningJoin.service.LightningJoinService;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Lightnings")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lightnings/participation")
public class LightningJoinController {

    private final LightningJoinService lightningJoinService;

    @Operation(summary = "번개 모임 참여", description = "번개 모임에 참여합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "모임에 참여되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 모임입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{lightningId}", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> participateLightningMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                               @PathVariable Long lightningId) {

        return ResponseEntity.status(201).body(SuccessResponse.successWithData(lightningJoinService.participateLightningMeeting(userDetails.getUsername(), lightningId)));
    }

    @Operation(summary = "번개 모임 참여 취소", description = "번개 모임에 참여를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "번개 모임에서 나갔습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 모임입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{lightningId}", method = RequestMethod.DELETE)
    public ResponseEntity<SuccessResponse<String>> leaveLightningMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                         @PathVariable Long lightningId) {

        return ResponseEntity.ok(SuccessResponse.successWithData(lightningJoinService.leaveLightningMeeting(userDetails.getUsername(), lightningId)));
    }

}
