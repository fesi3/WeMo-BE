package com.wemo.backend.domain.image.service;

import com.wemo.backend.domain.image.entity.Image;

import java.util.List;

public interface ImageReader {

    String getImage(Long entityId, Image.EntityType entityType);

    List<String> getImageList(Long entityId, Image.EntityType entityType);

}
