package com.wemo.backend.domain.review.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.review.dto.ReviewCreateRequest;
import com.wemo.backend.domain.review.dto.ReviewCreateResponse;
import com.wemo.backend.domain.review.service.ReviewService;
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

@Tag(name = "Reviews")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "후기 생성", description = "후기를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "후기가 등록되었습니다.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일정입니다.")
    })
    @RequestMapping(value = "/{planId}", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<ReviewCreateResponse>> createReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                              @PathVariable Long planId,
                                                                              @Valid @RequestBody ReviewCreateRequest request) {
        return ResponseEntity.status(201).body(SuccessResponse.successWithData(reviewService.createReview(userDetails.getUsername(), planId, request)));
    }
}
