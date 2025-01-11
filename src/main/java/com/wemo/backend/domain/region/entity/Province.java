package com.wemo.backend.domain.region.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TB_PROVINCE")
@Getter
@NoArgsConstructor
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "province_id", nullable = false)
    @Comment("시/도 id")
    private Long id;

    @Column(name = "province_name", nullable = false, unique = true)
    @Comment("시/도 이름")
    private String provinceName;

    public Province(String provinceName) {
        this.provinceName = provinceName;

    }

}
