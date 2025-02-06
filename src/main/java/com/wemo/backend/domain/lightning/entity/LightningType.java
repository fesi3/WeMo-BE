package com.wemo.backend.domain.lightning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "TB_LIGHTNING_TYPE")
@NoArgsConstructor
@AllArgsConstructor
public class LightningType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lightning_type_id", nullable = false)
    @Comment("번개 모임 종류 id")
    private long lightningTypeId;

    @Column(name = "light_type_name", nullable = false)
    @Comment("번개 모임 종류 이름")
    private String lightTypeName;

}
