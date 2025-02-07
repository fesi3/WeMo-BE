package com.wemo.backend.domain.lightningJoin.service;

import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightning.service.LightningReader;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import com.wemo.backend.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.wemo.backend.global.exception.ErrorCode.LIGHTNING_MEETING_IS_FULL;

@Slf4j
@Service
@RequiredArgsConstructor
public class LightningJoinServiceImpl implements LightningJoinService {

    private final UserReader userReader;

    private final LightningReader lightningReader;

    private final LightningJoinStore lightningJoinStore;

    private final LightningJoinReader lightningJoinReader;

    /**
     * 번개 모임 참여
     *
     * @param email       사용자 이메일
     * @param lightningId 번개 모임 id
     * @return 응답 메세지
     */
    @Override
    @Transactional
    public String participateLightningMeeting(String email, Long lightningId) {

        User user = userReader.getActiveUserByEmail(email);
        Lightning lightning = lightningReader.getLightningById(lightningId);

        int currentParticipants = lightningJoinReader.getParticipantsCount(lightning);
        if (currentParticipants >= lightning.getLightningCapacity())
            throw new CustomException(LIGHTNING_MEETING_IS_FULL);

        lightningJoinStore.store(user, lightning);

        log.info("{}가 번개 모임 id {} 에 참여하였습니다.", email, lightningId);

        return "모임에 참여되었습니다.";
    }

}
