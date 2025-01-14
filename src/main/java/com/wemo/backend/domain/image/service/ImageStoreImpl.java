package com.wemo.backend.domain.image.service;

import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.image.repository.ImageRepository;
import com.wemo.backend.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageStoreImpl implements ImageStore {

    private final ImageReader imageReader;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
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

    @Override
    @Transactional
    public List<String> storeImageList(User user, Long entityId, List<String> fileUrls, Image.EntityType entityType) {

        List<String> imageList = new ArrayList<>();

        for (int i = 0; i < fileUrls.size(); i++) {
            String fileUrl = fileUrls.get(i);

            // 이미지 생성
            Image image = Image.builder()
                    .user(user)
                    .entityType(entityType)
                    .entityId(entityId)
                    .fileUrl(fileUrl)
                    .build();

            // 첫 번째 이미지라면 대표 이미지로 설정
            if (i == 0) {
                image.updateMain(); // 첫 번째 이미지는 대표 이미지로 설정
            }

            // 이미지 저장
            imageRepository.save(image);

            // 저장된 이미지 URL을 리스트에 추가
            imageList.add(image.getFileUrl());
        }

        return imageList;
    }

    @Override
    public void updateImage(User user, Long entityId, List<String> fileUrls, Image.EntityType entityType) {
        // 기존 이미지 삭제
        List<Image> allByEntityIdAndEntityType = imageRepository.findAllByEntityIdAndEntityType(entityId, entityType);
        imageRepository.deleteAll(allByEntityIdAndEntityType);

        // 다시 저장
        storeImageList(user, entityId, fileUrls, entityType);
    }

}
