package com.wemo.backend.domain.lightning.service;

import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightning.repository.LightningRepository;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.wemo.backend.global.exception.ErrorCode.LIGHTNING_MEETING_NOT_FOUND;
import static com.wemo.backend.global.exception.ErrorCode.LIGHTNING_PERMISSION_DENIED;

@Component
@RequiredArgsConstructor
public class LightningReaderImpl implements LightningReader {

    private final LightningRepository lightningRepository;

    @Override
    public Lightning getLightningById(Long lightningId) {

        return lightningRepository.findById(lightningId).orElseThrow(
                () -> new CustomException(LIGHTNING_MEETING_NOT_FOUND)
        );
    }

    @Override
    public void validLightningUser(User user, Lightning lightning) {

        if (lightning.getUser() != user) throw new CustomException(LIGHTNING_PERMISSION_DENIED);

    }

}
