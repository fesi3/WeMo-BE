package com.wemo.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserReviewPagingResponse {

        private int reviewCount;

        private List<?> reviewList;

        private int pageSize;

        private int page;

        private int totalPage;

        public UserReviewPagingResponse(Page<UserReviewListResponse> reviewListResponses) {

            this.reviewCount = (int) reviewListResponses.getTotalElements();
            this.reviewList = reviewListResponses.getContent();
            this.pageSize = reviewListResponses.getSize();
            this.page = reviewListResponses.getPageable().getPageNumber() + 1;
            this.totalPage = reviewListResponses.getTotalPages();

        }
}
