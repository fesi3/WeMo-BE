package com.wemo.backend.domain.user.dto;

import com.wemo.backend.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class UserInfoResponse {

    private String email;

    private String nickname;

    private String profileImagePath;

    private String companyName;

    private String loginType;

    private LocalDateTime createdAt;

    public static UserInfoResponse fromEntity(User user) {

        return UserInfoResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImagePath(user.getProfileImagePath())
                .companyName(user.getCompanyName())
                .loginType(user.getLoginType().getUserType())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
