package com.wemo.backend.domain.image.service;

import com.wemo.backend.domain.image.entity.Image;

public interface ImageReader {

    Image getImage(Long entityId, Image.EntityType entityType);

}
