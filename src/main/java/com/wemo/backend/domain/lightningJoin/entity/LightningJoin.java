package com.wemo.backend.domain.lightningJoin.entity;

import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Builder
@Entity
@Table(name = "TB_LIGHTNING_JOIN")
@NoArgsConstructor
@AllArgsConstructor
public class LightningJoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lightning_join_id", nullable = false)
    @Comment("번개 모임 참여 id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("유저 id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lightning_id", nullable = false)
    private Lightning lightning;

}
