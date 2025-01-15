package com.wemo.backend.domain.like.service;

import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.service.PlanReader;
import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.service.UserReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final UserReader userReader;

    private final PlanReader planReader;

    private final LikeReader likeReader;

    private final LikeStore likeStore;

    /**
     * 일정 좋아요
     *
     * @param email  이메일
     * @param planId 일정 id
     * @return 성공 메세지
     */
    @Override
    @Transactional
    public String likePlan(String email, Long planId) {

        // 유효성 검사
        User user = userReader.getUserByEmail(email);
        Plan plan = planReader.getPlan(planId);

        // 이미 좋아요를 누른 일정이라면 예외 반환
        likeReader.validateLike(user, plan);

        likeStore.storeLike(user, plan);

        log.info("사용자 {}가 일정 id {}에 좋아요를 눌렀습니다.", user.getEmail(), plan.getId());

        return "일정에 좋아요를 눌렀습니다.";
    }

    /**
     * 일정 좋아요 취소
     *
     * @param email  이메일
     * @param planId 일정 id
     * @return 성공 메세지
     */
    @Override
    public String deleteLikePlan(String email, Long planId) {

        // 유효성 검사
        User user = userReader.getUserByEmail(email);
        Plan plan = planReader.getPlan(planId);

        // 좋아요를 누르지 않은 일정이라면 예외 반환
        likeReader.validateLikeToDelete(user, plan);

        likeStore.deleteLike(user, plan);

        log.info("사용자 {}가 일정 id {}에 누른 좋아요를 취소했습니다.", user.getEmail(), plan.getId());

        return "일정에 좋아요를 취소했습니다.";
    }

}
