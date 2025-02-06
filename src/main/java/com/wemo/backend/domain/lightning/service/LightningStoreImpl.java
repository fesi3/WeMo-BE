package com.wemo.backend.domain.lightning.service;

import com.wemo.backend.domain.lightning.dto.LightningCreateRequest;
import com.wemo.backend.domain.lightning.entity.DateType;
import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightning.entity.LightningType;
import com.wemo.backend.domain.lightning.repository.LightningRepository;
import com.wemo.backend.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LightningStoreImpl implements LightningStore {

    private final LightningRepository lightningRepository;

    @Override
    @Transactional
    public Lightning store(User user, LightningType lightningType, LightningCreateRequest request) {

        Lightning lightning = Lightning.builder()
                .user(user)
                .lightningName(request.getLightningName())
                .lightningType(lightningType)
                .dateType(DateType.fromId(request.getDateTypeId()))
                .lightningDate(request.getLightningDate())
                .lightningContent(request.getLightningContent())
                .lightningCapacity(request.getLightningCapacity())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        return lightningRepository.save(lightning);

    }

}
