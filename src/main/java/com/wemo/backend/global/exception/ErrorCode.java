package com.wemo.backend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // user
    ILLEGAL_EMAIL_DUPLICATION(HttpStatus.CONFLICT, "이미 사용 중인 이메일 입니다."),
    ILLEGAL_PASSWORD_NOT_VALID(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    ILLEGAL_USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "가입되지 않은 아이디입니다."),

    // token
    ILLEGAL_REFRESH_TOKEN_NOT_VALID(HttpStatus.BAD_REQUEST, "유효하지 않은 refreshToken 입니다."),

    // category
    ILLEGAL_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),

    // meeting
    ILLEGAL_MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 모임입니다."),

    // plan
    ILLEGAL_PLAN_NOT_GRANTED(HttpStatus.BAD_REQUEST, "모임장만 일정을 생성할 수 있습니다."),
    ILLEGAL_ADDRESS_NOT_VALID(HttpStatus.BAD_REQUEST, "주소 값이 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {

        this.httpStatus = httpStatus;
        this.message = message;
    }

}
