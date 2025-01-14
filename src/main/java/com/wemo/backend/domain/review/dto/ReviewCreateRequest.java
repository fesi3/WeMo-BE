package com.wemo.backend.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewCreateRequest {

    @Max(5)
    @NotNull(message = "평점은 0 ~ 5 사이의 정수 형태만 입력 가능합니다.")
    private int score;

    @NotBlank(message = "후기 내용은 필수 입력 값입니다.")
    @Size(min = 5, max = 500, message = "후기는 최소 5자, 최대 500자로 입력 가능합니다.")
    private String comment;

    private List<String> fileUrls;

}
