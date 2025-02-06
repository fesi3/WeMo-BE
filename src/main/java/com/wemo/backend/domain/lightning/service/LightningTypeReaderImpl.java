package com.wemo.backend.domain.lightning.service;

import com.wemo.backend.domain.lightning.entity.LightningType;
import com.wemo.backend.domain.lightning.repository.LightningTypeRepository;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.wemo.backend.global.exception.ErrorCode.LIGHTNING_TYPE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class LightningTypeReaderImpl implements LightningTypeReader {

    private final LightningTypeRepository lightningTypeRepository;

    @Override
    public LightningType getLightningType(Long lightningTypeId) {

        return lightningTypeRepository.findById(lightningTypeId).orElseThrow(
                () -> new CustomException(LIGHTNING_TYPE_NOT_FOUND)
        );
    }

}
