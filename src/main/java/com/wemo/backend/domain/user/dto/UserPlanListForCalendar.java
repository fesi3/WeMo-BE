package com.wemo.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserPlanListForCalendar {

    private List<UserPlanListResponseForCalendar> planList;

}
