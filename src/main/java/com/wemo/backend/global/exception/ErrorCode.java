package com.wemo.backend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // User
    EMAIL_ALREADY_IN_USE(HttpStatus.CONFLICT, "이미 사용 중인 이메일 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "가입되지 않은 아이디입니다."),
    USER_WITHDRAW(HttpStatus.BAD_REQUEST, "탈퇴한 사용자입니다."),

    // Token
    REFRESH_TOKEN_NOT_PROVIDED(HttpStatus.BAD_REQUEST, "refreshToken이 요청에 포함되지 않았습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 refreshToken 입니다."),

    // Category
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),

    // Meeting
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 모임입니다."),
    ALREADY_JOINED_MEETING(HttpStatus.BAD_REQUEST, "이미 가입된 모임입니다."),
    MEETING_PERMISSION_DENIED(HttpStatus.UNAUTHORIZED, "모임장만 가능한 기능입니다."),
    GROUP_LEADER_CANNOT_LEAVE(HttpStatus.FORBIDDEN, "모임장은 해당 모임에서 나갈 수 없습니다."),
    MEETING_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "가입되지 않은 모임입니다."),

    // Plan
    INVALID_ADDRESS(HttpStatus.BAD_REQUEST, "주소 값이 유효하지 않습니다."),
    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 일정입니다."),
    ALREADY_JOINED_PLAN(HttpStatus.BAD_REQUEST, "이미 참여한 일정입니다."),
    PLAN_IS_FULL(HttpStatus.BAD_REQUEST, "모집 정원이 마감된 일정입니다."),
    PLAN_ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일정에 참여 기록이 없습니다."),
    ALREADY_LIKED_PLAN(HttpStatus.BAD_REQUEST, "이미 좋아요한 일정입니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 누르지 않은 일정입니다."),
    HOST_ATTENDANCE_REQUIRED(HttpStatus.BAD_REQUEST, "주최자는 참여가 필수입니다."),

    // Review
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 후기를 등록한 일정입니다."),
    REVIEW_CREATION_BEFORE_PLAN_END(HttpStatus.BAD_REQUEST, "일정이 완료되지 않아 후기를 작성할 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 후기입니다."),
    REVIEW_PERMISSION_DENIED(HttpStatus.UNAUTHORIZED, "본인이 작성한 후기만 수정/삭제가 가능합니다."),

    // Image
    IMAGE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지는 한 번에 최대 10개까지 요청 가능합니다."),

    // Region
    PROVINCE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 시/도 데이터입니다."),

    // OAuth2
    REST_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "REST 요청 처리에 실패했습니다."),
    INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 로그인 타입입니다."),
    INVALID_RESPONSE(HttpStatus.BAD_REQUEST, "유효하지 않은 응답입니다."),

    // LightningType
    LIGHTNING_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 번개모임 종류입니다."),

    // Lightning
    LIGHTNING_MEETING_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 번개 모임입니다."),
    ALREADY_JOINED_LIGHTNING_MEETING(HttpStatus.BAD_REQUEST, "이미 참여된 모임입니다."),
    LIGHTNING_MEETING_IS_FULL(HttpStatus.BAD_REQUEST, "정원이 마감된 모임입니다."),
    LIGHTNING_PERMISSION_DENIED(HttpStatus.UNAUTHORIZED, "본인이 생성한 모임만 수정/삭제 가능합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {

        this.httpStatus = httpStatus;
        this.message = message;
    }
}
