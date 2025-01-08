package com.wemo.backend.domain.user.controller;

import com.wemo.backend.domain.user.dto.EmailCheckRequest;
import com.wemo.backend.domain.user.dto.UserCreateRequest;
import com.wemo.backend.domain.user.service.UserService;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auths")
public class UserController {

    private final UserService userService;

    @Operation(summary = "이메일 중복 검사", description = "이메일 중복 검사합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 이메일입니다..",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.")
    })
    @RequestMapping(value = "/check-email", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> checkEmail(@RequestBody EmailCheckRequest request) {
        userService.checkEmail(request.getEmail());
        return ResponseEntity.ok(SuccessResponse.successWithNoData("사용 가능한 이메일입니다."));
    }

    @Operation(summary = "회원 가입", description = "사용자를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "사용자가 생성되었습니다.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.")
    })
    @RequestMapping(value = "/signup",method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> createUser(@Valid @RequestBody UserCreateRequest request) {

        userService.createUser(request);
        return ResponseEntity.status(201).body(SuccessResponse.successWithNoData("사용자 생성 성공"));
    }
}
