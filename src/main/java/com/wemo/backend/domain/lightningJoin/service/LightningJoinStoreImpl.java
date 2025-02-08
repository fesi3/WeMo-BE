package com.wemo.backend.domain.lightningJoin.service;

import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightningJoin.entity.LightningJoin;
import com.wemo.backend.domain.lightningJoin.repository.LightningJoinRepository;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.wemo.backend.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class LightningJoinStoreImpl implements LightningJoinStore {

    private final LightningJoinRepository lightningJoinRepository;

    @Override
    @Transactional
    public void store(User user, Lightning lightning) {

        if (isUserJoinedLightning(user, lightning)) {
            throw new CustomException(ALREADY_JOINED_LIGHTNING_MEETING);
        }

        LightningJoin lightningJoin = LightningJoin.builder()
                .user(user)
                .lightning(lightning)
                .build();

        lightningJoinRepository.save(lightningJoin);
    }

    @Override
    @Transactional
    public void delete(User user, Lightning lightning) {

        if (lightning.getUser().getEmail().equals(user.getEmail())) throw new CustomException(HOST_ATTENDANCE_REQUIRED);

        if (!isUserJoinedLightning(user, lightning)) {
            throw new CustomException(LIGHTNING_MEETING_ATTENDANCE_NOT_FOUND);
        }

        lightningJoinRepository.deleteByUserAndLightning(user, lightning);
    }

    private boolean isUserJoinedLightning(User user, Lightning lightning) {

        return lightningJoinRepository.existsByUserAndLightning(user, lightning);
    }

}
