package com.wemo.backend.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewPagingResponse {

    private int reviewCount;

    private List<?> reviewList;

    private int pageSize;

    private int page;

    private int totalPage;

    public ReviewPagingResponse(Page<ReviewListResponse> reviewList) {

        this.reviewCount = (int) reviewList.getTotalElements();
        this.reviewList = reviewList.getContent();
        this.pageSize = reviewList.getSize();
        this.page = reviewList.getPageable().getPageNumber() + 1;
        this.totalPage = reviewList.getTotalPages();

    }

}
