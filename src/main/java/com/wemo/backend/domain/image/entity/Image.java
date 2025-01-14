package com.wemo.backend.domain.image.entity;

import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TB_IMAGE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    @Comment("이미지 id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 이미지와 사용자 관계 설정 (다대일)
    @JoinColumn(name = "user_id", nullable = false)  // user_id 외래키 설정
    @Comment("유저 id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    @Comment("엔티티 타입")
    private EntityType entityType;

    @Column(name = "entity_id", nullable = false)
    @Comment("엔티티 id")
    private Long entityId;

    @Column(name = "file_url", nullable = false)
    @Comment("이미지 url")
    private String fileUrl;

    @Column(name = "is_main", nullable = false)
    @Comment("대표 이미지 여부")
    @Builder.Default
    private boolean main = false;

    public void updateMain() {

        this.main = true;
    }

    public enum EntityType {

        PROFILE("유저 프로필"),
        MEETING("모임"),
        PLAN("일정"),
        REVIEW("후기");

        private String entityType;

        EntityType(String entityType) {
            this.entityType = entityType;
        }
    }

}
