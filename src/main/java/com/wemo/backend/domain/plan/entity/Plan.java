package com.wemo.backend.domain.plan.entity;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.region.entity.District;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_PLAN")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id", nullable = false)
    @Comment("일정 id")
    private Long id;

    @Column(name = "plan_name", nullable = false)
    @Comment("일정명")
    private String planName;

    @Column(name = "content")
    @Comment("일정 내용")
    private String content;

    @Column(name = "date_time", nullable = false)
    @Comment("모임 날짜")
    private LocalDateTime dateTime;

    @Column(name = "registration_end", nullable = false)
    @Comment("마감 날짜")
    private LocalDateTime registrationEnd;

    @Column(name = "capacity", nullable = false)
    @Comment("모집 정원")
    private int capacity;

    @Column(name = "longitude", nullable = false)
    @Comment("경도")
    private double longitude;

    @Column(name = "latitude", nullable = false)
    @Comment("위도")
    private double latitude;

    @Column(name = "address", nullable = false)
    @Comment("주소")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    @Comment("군/구 id")
    private District district;

    @Column(name = "is_opened", nullable = false)
    @Comment("개설 확정 여부")
    @Builder.Default
    private boolean opened = false;

    @Column(name = "is_canceled", nullable = false)
    @Comment("취소 여부")
    @Builder.Default
    private boolean canceled = false;

    @Column(name = "is_fulled", nullable = false)
    @Comment("마감 여부")
    @Builder.Default
    private boolean fulled = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("유저 id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    @Comment("모임 id")
    private Meeting meeting;

    @Column(name = "view_count")
    @Comment("조회수")
    private int viewCount;

    public void open() {

        this.opened = true;
    }

    public void close() {

        this.fulled = true;
    }

    public void updateViewCount() {

        this.viewCount++;

    }

}
