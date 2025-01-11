package com.wemo.backend.domain.region.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TB_DISTRICT")
@Getter
@NoArgsConstructor
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id", nullable = false)
    @Comment("군/구 id")
    private Long id;

    @Column(name = "district_name", nullable = false)
    @Comment("군/구 이름")
    private String districtName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    @Comment("시/도 id")
    private Province province;

    public District(String districtName, Province province) {

        this.districtName = districtName;
        this.province = province;
    }

}
