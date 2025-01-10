package com.wemo.backend.domain.user.controller;

import com.wemo.backend.domain.user.dto.EmailCheckRequest;
import com.wemo.backend.domain.user.dto.SigninRequest;
import com.wemo.backend.domain.user.dto.UserCreateRequest;
import com.wemo.backend.domain.user.service.UserService;
import com.wemo.backend.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Users")
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
    public ResponseEntity<SuccessResponse<String>> signup(@Valid @RequestBody UserCreateRequest request) {

        userService.signup(request);
        return ResponseEntity.status(201).body(SuccessResponse.successWithNoData("사용자 생성 성공"));
    }

    @Operation(summary = "로그인", description = "사용자가 로그인을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인에 성공하였습니다.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.")
    })
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> signin(@Valid @RequestBody SigninRequest request) {

        HttpHeaders httpHeaders = userService.signin(request);
        return ResponseEntity.status(200).headers(httpHeaders).body(SuccessResponse.successWithNoData("로그인 성공"));
    }

    @Operation(summary = "로그아웃", description = "사용자가 로그아웃을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃에 성공하였습니다.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.")
    })
    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse<String>> signout(
                                          @RequestHeader("Authorization") String accessToken,
                                          @RequestHeader("Refresh-Token") String refreshToken) {
        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);

        return ResponseEntity.ok().body(SuccessResponse.successWithNoData(userService.signout(accessToken, refreshToken)));
    }

}
