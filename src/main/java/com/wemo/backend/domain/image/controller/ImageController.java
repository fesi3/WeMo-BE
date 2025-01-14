package com.wemo.backend.domain.image.controller;

import com.wemo.backend.domain.auth.UserDetailsImpl;
import com.wemo.backend.domain.image.dto.PresignedUrlResponse;
import com.wemo.backend.domain.image.service.S3Service;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Images")
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    
    private final S3Service s3Service;

    @Operation(summary = "이미지 업로드 요청", description = "presignedUrl 정보를 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "presignedUrl 목록이 반환되었습니다."),
            @ApiResponse(responseCode = "400", description = "이미지 업로드는 최대 10개까지 요청 가능합니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse<PresignedUrlResponse>> getPresignedUrl(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                      @RequestParam int count) {

        return ResponseEntity.ok(SuccessResponse.successWithData(s3Service.generatePresignedUrl(count)));

    }

}
