package com.wemo.backend.domain.review.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.review.dto.ReviewCreateRequest;
import com.wemo.backend.domain.review.dto.ReviewCreateResponse;
import com.wemo.backend.domain.review.dto.ReviewDetailResponse;
import com.wemo.backend.domain.review.dto.ReviewPagingResponse;
import com.wemo.backend.domain.review.service.ReviewService;
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

@Tag(name = "Reviews")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "후기 생성", description = "후기를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "후기가 등록되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일정입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{planId}", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<ReviewCreateResponse>> createReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                              @PathVariable Long planId,
                                                                              @Valid @RequestBody ReviewCreateRequest request) {

        return ResponseEntity.status(201).body(SuccessResponse.successWithData(reviewService.createReview(userDetails.getUsername(), planId, request)));
    }

    @Operation(summary = "후기 목록 조회", description = "회원 또는 비회원이 요청하는 후기의 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청한 후기 목록이 반환되었습니다.")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<ReviewPagingResponse>> getReviewList(@RequestParam(defaultValue = "1") int page,
                                                                               @RequestParam(required = false, defaultValue = "10") int size,
                                                                               @RequestParam(required = false) String province,
                                                                               @RequestParam(required = false) String district,
                                                                               @RequestParam(required = false) String startDate,
                                                                               @RequestParam(required = false) String endDate,
                                                                               @RequestParam(required = false) Long categoryId,
                                                                               @RequestParam(required = false) String sort) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(SuccessResponse.successWithData(reviewService.getReviewList(pageable, province, district, startDate, endDate, categoryId, sort)));
    }

    @Operation(summary = "후기 수정", description = "후기를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "후기가 수정되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 후기입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{reviewId}", method = RequestMethod.PUT)
    public ResponseEntity<SuccessResponse<ReviewCreateResponse>> updateReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                              @PathVariable Long reviewId,
                                                                              @Valid @RequestBody ReviewCreateRequest request) {

        return ResponseEntity.ok(SuccessResponse.successWithData(reviewService.updateReview(userDetails.getUsername(), reviewId, request)));
    }

    @Operation(summary = "후기 삭제", description = "후기를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "후기가 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 후기입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{reviewId}", method = RequestMethod.DELETE)
    public ResponseEntity<SuccessResponse<String>> deleteReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @PathVariable Long reviewId) {

        return ResponseEntity.ok(SuccessResponse.successWithNoData(reviewService.deleteReview(userDetails.getUsername(), reviewId)));
    }

    @Operation(summary = "후기 상세조회", description = "회원 또는 비회원이 요청한 후기의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "후기가 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 후기입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "/{reviewId}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<ReviewDetailResponse>> getReviewDetail(@PathVariable Long reviewId) {

        return ResponseEntity.ok(SuccessResponse.successWithData(reviewService.getReviewDetail(reviewId)));
    }

}
