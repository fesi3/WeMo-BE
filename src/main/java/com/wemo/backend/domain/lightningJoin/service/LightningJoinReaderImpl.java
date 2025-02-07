package com.wemo.backend.domain.lightningJoin.service;

import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightningJoin.repository.LightningJoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LightningJoinReaderImpl implements LightningJoinReader {

    private final LightningJoinRepository lightningJoinRepository;

    @Override
    public int getParticipantsCount(Lightning lightning) {

        return (int) lightningJoinRepository.countByLightning(lightning);
    }

}
