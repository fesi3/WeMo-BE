package com.wemo.backend.domain.image.service;

import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageReaderImpl implements ImageReader {

    private final ImageRepository imageRepository;

    @Override
    public Image getImage(Long entityId, Image.EntityType entityType) {

        return imageRepository.findByEntityIdAndEntityType(entityId, entityType);
    }

}
