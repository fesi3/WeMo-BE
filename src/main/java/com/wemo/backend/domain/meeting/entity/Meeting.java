package com.wemo.backend.domain.meeting.entity;

import com.wemo.backend.domain.category.entity.Category;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TB_MEETING")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meeting extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id", nullable = false)
    @Comment("모임 id")
    private Long id;

    @Column(name = "meeting_name", nullable = false)
    @Comment("모임명")
    private String meetingName;

    @Column(name = "description")
    @Comment("모임설명")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id") // 외래 키 컬럼 이름을 명시적으로 지정
    @Comment("카테고리 id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("유저 id")
    private User user;

}
