package com.wemo.backend.domain.image.service;

import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.user.entity.User;

public interface ImageStore {

    Image storeImage(User user, Long entityId, String fileUrl, Image.EntityType entityType);

}
