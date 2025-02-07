package com.wemo.backend.domain.lightning.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DateType {

    MORNING(1, "출근 전"),
    LUNCH(2, "점심시간"),
    EVENING(3, "퇴근 후");

    private final int id;
    private final String name;

    // id 값으로 DateType 찾는 메서드
    public static DateType fromId(int id) {

        return Arrays.stream(DateType.values())
                .filter(dateType -> dateType.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 번개 모임 시간 타입입니다."));
    }
    
    public String getDisplayName() {

        return this.name;
    }
}
