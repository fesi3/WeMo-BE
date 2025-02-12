package com.wemo.backend.domain.lightning.entity;

import com.wemo.backend.global.exception.CustomException;
import com.wemo.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DateType {

    MORNING(1, "출근 전", 6, 10),
    LUNCH(2, "점심시간", 12, 14),
    EVENING(3, "퇴근 후", 17, 24);

    private final int id;
    private final String name;
    private final int startHour;
    private final int endHour;

    // id 값으로 DateType 찾는 메서드
    public static DateType fromId(int id) {

        return Arrays.stream(DateType.values())
                .filter(dateType -> dateType.id == id)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.LIGHTNING_TIME_TYPE_NOT_FOUND));
    }

    // 시간(hour) 값으로 DateType 찾는 메서드
    public static DateType fromHour(int hour) {

        return Arrays.stream(DateType.values())
                .filter(dateType -> hour >= dateType.startHour && hour < dateType.endHour)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.LIGHTNING_TIME_TYPE_NOT_FOUND));
    }

    public static DateType typeConverter(LocalDateTime dateTime) {

        return fromHour(dateTime.getHour());
    }

    public String getDisplayName() {

        return this.name;
    }
}
