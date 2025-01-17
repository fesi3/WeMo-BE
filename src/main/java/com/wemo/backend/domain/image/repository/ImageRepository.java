package com.wemo.backend.domain.image.repository;

import com.wemo.backend.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByEntityIdAndEntityTypeAndMainIsTrue(Long entityId, Image.EntityType entityType);

    List<Image> findAllByEntityIdAndEntityType(Long entityId, Image.EntityType entityType);

}
