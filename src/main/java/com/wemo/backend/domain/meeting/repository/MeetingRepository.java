package com.wemo.backend.domain.meeting.repository;

import com.wemo.backend.domain.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingQueryDsl {

    Optional<Meeting> findByIdAndDeletedAtIsNull(Long meetingId);

}
