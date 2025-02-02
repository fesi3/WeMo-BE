package com.wemo.backend.domain.image.service;

import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.user.entity.User;

import java.util.List;

public interface ImageReader {

    String getMainImage(Long entityId, Image.EntityType entityType);

    List<String> getImageList(Long entityId, Image.EntityType entityType);

    List<Image> getImageListByUser(User user);

}
