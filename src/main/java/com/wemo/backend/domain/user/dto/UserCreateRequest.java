package com.wemo.backend.domain.user.dto;

import com.wemo.backend.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 최소 2자, 최대 20자여야 합니다.")
    private String nickname;

    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    private String companyName;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String passwordCheck;

    public User createUser() {

        return User.builder()
                .email(email)
                .nickname(nickname)
                .companyName(companyName)
                .password(password)
                .build();
    }

}
