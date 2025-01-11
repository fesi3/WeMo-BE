package com.wemo.backend.domain.review.entity;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.entity.Timestamped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TB_REVIEW")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    @Comment("후기 id")
    private Long id;

    @Column(name = "score", nullable = false)
    @Min(0)
    @Max(5)
    @Comment("후기 평점")
    private int score;

    @Column(name = "comment", nullable = false)
    @Size(min = 5, max = 500)
    @Comment("후기 내용")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @Comment("유저 id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    @Comment("일정 id")
    private Plan plan;

}
