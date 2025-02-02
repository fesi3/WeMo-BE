package com.wemo.backend.domain.meeting.repository;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingQueryDsl {

    List<Meeting> findAllByUserAndDeletedAtIsNull(User user);

}
