package com.wemo.backend.domain.user.entity;

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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "USER_ID", nullable = false)
    @Comment("유저 id")
    private UUID id;

    @Column(name = "EMAIL", nullable = false, unique = true)
    @Comment("이메일")
    private String email;

    @Column(name = "COMPANY_NAME", nullable = false)
    @Comment("회사명")
    private String companyName;

    @Column(name = "NICKNAME", nullable = false)
    @Comment("닉네임")
    private String nickname;

    @Column(name = "PASSWORD", nullable = false)
    @Comment("비밀번호")
    private String password;

    @Column(name = "PROFILE_IMAGE_PATH", nullable = false)
    @Comment("프로필 이미지")
    private String profileImagePath;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "LOGIN_TYPE")
    private LoginType loginType;

}
