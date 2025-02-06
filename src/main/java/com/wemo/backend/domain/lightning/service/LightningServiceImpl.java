package com.wemo.backend.domain.lightning.service;

import com.wemo.backend.domain.lightning.dto.LightningCreateRequest;
import com.wemo.backend.domain.lightning.dto.LightningCreateResponse;
import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightning.entity.LightningType;
import com.wemo.backend.domain.lightningJoin.service.LightningJoinStore;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LightningServiceImpl implements LightningService {

    private final UserReader userReader;

    private final LightningTypeReader lightningTypeReader;

    private final LightningStore lightningStore;

    private final LightningJoinStore lightningJoinStore;

    /**
     * 번개 모임 생성
     *
     * @param email   사용자 이메일
     * @param request 번개 모임 생성 데이터
     * @return 생성된 번개 모임 정보
     */
    @Override
    @Transactional
    public LightningCreateResponse createLightnings(String email, LightningCreateRequest request) {

        User user = userReader.getUserByEmail(email);
        LightningType lightningType = lightningTypeReader.getLightningType(request.getLightningTypeId());
        Lightning lightning = lightningStore.store(user, lightningType, request);

        // 주최자는 자동 참여
        lightningJoinStore.store(user, lightning);

        return LightningCreateResponse.fromEntity(lightning);
    }

}
