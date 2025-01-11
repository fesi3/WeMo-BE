package com.wemo.backend.domain.user.entity;

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

    @Column(name = "company_name", nullable = false)
    @Comment("회사명")
    private String companyName;

    @Column(name = "nickname", nullable = false)
    @Comment("닉네임")
    private String nickname;

    @Column(name = "password", nullable = false)
    @Comment("비밀번호")
    private String password;

    @Column(name = "profile_image_path", nullable = false)
    @Comment("프로필 이미지")
    private String profileImagePath;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "login_type")
    private LoginType loginType;

}
