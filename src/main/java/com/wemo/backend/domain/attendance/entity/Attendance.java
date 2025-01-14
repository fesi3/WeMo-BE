package com.wemo.backend.domain.attendance.entity;


import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TB_ATTENDANE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id", nullable = false)
    @Comment("참석 id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("유저 id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    @Comment("일정 id")
    private Plan plan;

    @Column(name = "is_reviewed", nullable = false)
    @Comment("후기 작성 여부")
    @Builder.Default
    private boolean reviewed = false;


    public void updateStatus() {

        this.reviewed = true;

    }

}
