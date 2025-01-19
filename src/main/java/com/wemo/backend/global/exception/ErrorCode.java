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
    ALREADY_JOINED_MEETING(HttpStatus.BAD_REQUEST, "이미 가입된 모임입니다."),
    ILLEGAL_MEETING_GRANTED(HttpStatus.UNAUTHORIZED, "모임장만 가능한 기능입니다."),

    // plan
    ILLEGAL_ADDRESS_NOT_VALID(HttpStatus.BAD_REQUEST, "주소 값이 유효하지 않습니다."),
    ILLEGAL_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 일정입니다."),
    ALREADY_JOINED_PLAN(HttpStatus.BAD_REQUEST, "이미 참여한 일정입니다."),
    PLAN_IS_FULLED(HttpStatus.BAD_REQUEST, "모집 정원이 마감된 일정입니다."),
    PLAN_ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일정에 참여 기록이 없습니다."),
    ALREADY_LIKED_PLAN(HttpStatus.BAD_REQUEST, "이미 좋아요한 일정입니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 누르지 않은 일정입니다."),
    USER_ATTENDANCE_NECESSARY(HttpStatus.BAD_REQUEST, "주최자는 필수 참여입니다."),

    // review
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 후기를 등록한 일정입니다."),
    REVIEW_CREATION_BEFORE_PLAN_END(HttpStatus.BAD_REQUEST, "일정이 완료되지 않아 후기를 작성할 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 후기입니다."),
    ILLEGAL_REVIEW_GRANTED(HttpStatus.UNAUTHORIZED, "본인이 작성한 후기만 수정/삭제가 가능합니다."),

    // image
    ILLEGAL_IMAGE_COUNT(HttpStatus.BAD_REQUEST, "이미지는 한 번에 최대 10개까지 요청 가능합니다."),

    // region
    PROVINCE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 시/도 데이터입니다."),

    // Oauth2
    REST_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "REST 요청 처리에 실패했습니다."),
    INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 로그인 타입입니다."),
    INVALID_RESPONSE(HttpStatus.BAD_REQUEST, "유효하지 않은 응답입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {

        this.httpStatus = httpStatus;
        this.message = message;
    }

}
