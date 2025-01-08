package com.wemo.backend.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginType {

    GENERAL("일반유저"),
    KAKAO("카카오유저"),
    GOOGLE("구글유저"),
    NAVER("네이버유저");

    private final String userType;

}
