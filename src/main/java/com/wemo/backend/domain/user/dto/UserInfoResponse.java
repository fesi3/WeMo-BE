package com.wemo.backend.domain.user.dto;

import com.wemo.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserInfoResponse {

    private String email;

    private String nickname;

    private String profileImagePath;

    private String companyName;

    private String loginType;

    private LocalDateTime createdAt;

    private int joinedPlanCount;

    private int likedPlanCount;

    private int writtenReviewCount;

    public static UserInfoResponse fromEntity(User user, int joinedPlanCount, int likedPlanCount, int writtenReviewCount) {

        return UserInfoResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImagePath(user.getProfileImagePath())
                .companyName(user.getCompanyName())
                .loginType(user.getLoginType().getLoginType())
                .createdAt(user.getCreatedAt())
                .joinedPlanCount(joinedPlanCount)
                .likedPlanCount(likedPlanCount)
                .writtenReviewCount(writtenReviewCount)
                .build();
    }

}
