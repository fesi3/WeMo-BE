package com.wemo.backend.domain.user.controller;

import com.wemo.backend.domain.auth.token.service.RefreshTokenManager;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Tokens")
@RestController
@RequiredArgsConstructor
public class TokenController {

    private final RefreshTokenManager refreshTokenManager;

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰이 재발급되었습니다.")
    })
    @RequestMapping(value = "/api/auths/reissue", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> reissueToken(HttpServletRequest request,
                                                                HttpServletResponse response) {

        HttpHeaders httpHeaders = refreshTokenManager.reissueToken(request, response);
        return ResponseEntity.status(200).headers(httpHeaders).body(SuccessResponse.successWithNoData("accessToken 재발급 성공"));
    }

}
