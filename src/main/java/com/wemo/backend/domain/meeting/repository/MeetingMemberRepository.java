package com.wemo.backend.domain.meeting.repository;

import com.wemo.backend.domain.meeting.entity.Meeting;
import com.wemo.backend.domain.meeting.entity.MeetingMember;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {

    boolean existsByUserAndMeeting(User user, Meeting meeting);

}
