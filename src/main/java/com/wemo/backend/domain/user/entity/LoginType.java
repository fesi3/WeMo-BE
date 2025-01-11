package com.wemo.backend.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginType {

    GENERAL("일반"),
    KAKAO("카카오"),
    GOOGLE("구글"),
    NAVER("네이버");

    private final String userType;

}
