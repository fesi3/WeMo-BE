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
    public void storeMeetingImage(User user, Long meetingId, String fileUrl) {

        Image image = Image.builder()
                .user(user)
                .entityType(Image.EntityType.MEETING)
                .entityId(meetingId)
                .fileUrl(fileUrl)
                .build();

        imageRepository.save(image);
    }

}
