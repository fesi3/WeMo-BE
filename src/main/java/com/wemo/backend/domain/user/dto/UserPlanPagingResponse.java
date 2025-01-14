package com.wemo.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserPlanPagingResponse {

    private int planCount;

    private List<?> planList;

    private int pageSize;

    private int page;

    private int totalPage;

    public UserPlanPagingResponse(Page<?> userPlanList) {

        this.planCount = (int) userPlanList.getTotalElements();
        this.planList = userPlanList.getContent();
        this.pageSize = userPlanList.getSize();
        this.page = userPlanList.getPageable().getPageNumber() + 1;
        this.totalPage = userPlanList.getTotalPages();

    }

}
