package com.wemo.backend.domain.image.repository;

import com.wemo.backend.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByEntityIdAndEntityType(Long entityId, Image.EntityType entityType);

}
