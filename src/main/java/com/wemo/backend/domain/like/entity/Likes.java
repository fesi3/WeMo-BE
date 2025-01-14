package com.wemo.backend.domain.like.entity;

import com.querydsl.core.annotations.QueryEntity;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TB_LIKE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@QueryEntity
public class Likes extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id", nullable = false)
    @Comment("좋아요 id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("유저 id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    @Comment("일정 id")
    private Plan plan;

}
