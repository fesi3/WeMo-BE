package com.wemo.backend.domain.image.service;

import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.user.entity.User;

import java.util.List;

public interface ImageStore {

    Image storeImage(User user, Long entityId, String fileUrls, Image.EntityType entityType);

    List<String> storeImageList(User user, Long entityId, List<String> fileUrls, Image.EntityType entityType);

    void updateImage(User user, Long entityId, List<String> fileUrls, Image.EntityType entityType);

}
