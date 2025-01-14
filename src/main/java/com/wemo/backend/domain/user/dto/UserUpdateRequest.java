package com.wemo.backend.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 최소 2자, 최대 20자여야 합니다.")
    private String nickname;

    private String fileUrl;

}
