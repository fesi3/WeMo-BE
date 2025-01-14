package com.wemo.backend.domain.user.dto;

import com.wemo.backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserUpdateResponse {

    private String nickname;

    private String profileImageUrl;

    public static UserUpdateResponse fromEntity(User updateUser) {

        return UserUpdateResponse.builder()
                .nickname(updateUser.getNickname())
                .profileImageUrl(updateUser.getProfileImagePath()).build();
    }

}
