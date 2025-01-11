package com.wemo.backend.domain.image.service;

import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.image.repository.ImageRepository;
import com.wemo.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageStoreImpl implements ImageStore {

    private final ImageRepository imageRepository;

    @Override
    public Image storeImage(User user, Long entityId, String fileUrl, Image.EntityType entityType) {

        Image image = Image.builder()
                .user(user)
                .entityType(entityType)
                .entityId(entityId)
                .fileUrl(fileUrl)
                .build();

        imageRepository.save(image);

        return image;
    }

}
