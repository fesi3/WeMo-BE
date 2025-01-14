package com.wemo.backend.domain.plan.service;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.plan.entity.Plan;
import com.wemo.backend.domain.plan.repository.PlanRepository;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wemo.backend.global.exception.ErrorCode.ILLEGAL_PLAN_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class PlanReaderImpl implements PlanReader {

    private final PlanRepository planRepository;


    @Override
    public Plan getPlan(Long planId) {

        return planRepository.findById(planId).orElseThrow(
                () -> new CustomException(ILLEGAL_PLAN_NOT_FOUND)
        );
    }

    @Override
    public List<Plan> getPlanByMeeting(Meeting meeting) {

        return planRepository.findAllByMeeting(meeting);
    }

    @Override
    public void delete(Plan plan) {

        planRepository.delete(plan);

    }

}
