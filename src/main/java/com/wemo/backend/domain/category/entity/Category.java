package com.wemo.backend.domain.category.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TB_CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    @Comment("카테고리 id")
    private Long id;

    @Column(name = "parent_id")
    @Comment("상위 카테고리 id")
    private Long parentId;

    @Column(name = "category_name", nullable = false)
    @Comment("카테고리 이름")
    private String categoryName;

}
