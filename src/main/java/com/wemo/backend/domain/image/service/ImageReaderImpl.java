package com.wemo.backend.domain.image.service;

import com.wemo.backend.domain.image.entity.Image;
import com.wemo.backend.domain.image.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageReaderImpl implements ImageReader {

    private final ImageRepository imageRepository;

    @Override
    public String getImage(Long entityId, Image.EntityType entityType) {

        return imageRepository.findByEntityIdAndEntityType(entityId, entityType).getFileUrl();
    }

    @Override
    public List<String> getImageList(Long entityId, Image.EntityType entityType) {

        List<String> fileUrlList = new ArrayList<>();
        List<Image> imageList = imageRepository.findAllByEntityIdAndEntityType(entityId, entityType);

        for (Image image : imageList) {
            fileUrlList.add(image.getFileUrl());
        }

        return fileUrlList;

    }

    @Override
    @Transactional
    public void deleteImage(Long entityId, Image.EntityType entityType) {
        List<Image> imageList = imageRepository.findAllByEntityIdAndEntityType(entityId, entityType);
        imageRepository.deleteAll(imageList); // 이미지 삭제
    }

}
