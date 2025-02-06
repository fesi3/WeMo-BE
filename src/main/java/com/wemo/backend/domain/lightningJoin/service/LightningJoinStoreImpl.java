package com.wemo.backend.domain.lightningJoin.service;

import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightningJoin.entity.LightningJoin;
import com.wemo.backend.domain.lightningJoin.repository.LightningJoinRepository;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.wemo.backend.global.exception.ErrorCode.ALREADY_JOINED_LIGHTNING_MEETING;

@Component
@RequiredArgsConstructor
public class LightningJoinStoreImpl implements LightningJoinStore {

    private final LightningJoinRepository lightningJoinRepository;

    @Override
    @Transactional
    public void store(User user, Lightning lightning) {

        boolean isJoined = lightningJoinRepository.existsByUserAndLightning(user, lightning);

        if (!isJoined) {

            LightningJoin lightningJoin = LightningJoin.builder()
                    .user(user)
                    .lightning(lightning)
                    .build();

            lightningJoinRepository.save(lightningJoin);
        } else {
            throw new CustomException(ALREADY_JOINED_LIGHTNING_MEETING);
        }
    }

}
