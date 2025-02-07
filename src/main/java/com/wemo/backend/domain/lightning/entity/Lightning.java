package com.wemo.backend.domain.lightning.entity;

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

@Getter
@Builder
@Entity
@Table(name = "TB_LIGHTNING")
@NoArgsConstructor
@AllArgsConstructor
public class Lightning extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lightning_id", nullable = false)
    @Comment("번개 모임 id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("유저 id")
    private User user;

    @Column(name = "lightning_name", nullable = false)
    @Comment("모임명")
    private String lightningName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lightning_type_id", nullable = false)
    @Comment("모임 종류")
    private LightningType lightningType;

    @Enumerated(EnumType.STRING)
    @Column(name = "date_type", nullable = false)
    @Comment("모임 시간 타입")
    private DateType dateType;

    @Column(name = "lightning_date", nullable = false)
    @Comment("모임 날짜")
    private LocalDateTime lightningDate;

    @Column(name = "lightning_content", nullable = false)
    @Comment("모임 내용")
    private String lightningContent;

    @Column(name = "lightning_capacity", nullable = false)
    @Comment("모임 모집 정원")
    private int lightningCapacity;

    @Column(name = "address", nullable = false)
    @Comment("모임 장소")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    @Comment("군/구 id")
    private District district;

    @Column(name = "latitude", nullable = false)
    @Comment("위도")
    private double latitude;

    @Column(name = "longitude", nullable = false)
    @Comment("경도")
    private double longitude;

}
