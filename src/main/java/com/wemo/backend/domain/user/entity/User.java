package com.wemo.backend.domain.user.entity;

import com.wemo.backend.domain.user.dto.AdditionalDataRequest;
import com.wemo.backend.domain.user.dto.UserUpdateRequest;
import com.wemo.backend.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.UUID;

@Entity
@Table(name = "TB_USER")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false)
    @Comment("유저 id")
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    @Comment("이메일")
    private String email;

    @Column(name = "company_name")
    @Comment("회사명")
    private String companyName;

    @Column(name = "nickname", nullable = false)
    @Comment("닉네임")
    private String nickname;

    @Column(name = "password")
    @Comment("비밀번호")
    private String password;

    @Column(name = "profile_image_path", nullable = false)
    @Comment("프로필 이미지")
    private String profileImagePath;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "login_type")
    private LoginType loginType = LoginType.GENERAL;

    public User(String email, String nickname, String profileImageUrl, LoginType loginType, String companyName, String password) {

        this.email = email;
        this.nickname = nickname;
        this.profileImagePath = profileImageUrl;
        this.loginType = loginType;
        this.companyName = companyName;
        this.password = password;

    }

    public User update(UserUpdateRequest request) {

        this.nickname = request.getNickname();
        this.profileImagePath = request.getFileUrl();
        return this;
    }

    public void saveAdditionalData(AdditionalDataRequest request) {

        this.companyName = request.getCompanyName();

    }

}
