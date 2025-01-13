package com.wemo.backend.domain.user.repository;

import com.wemo.backend.domain.user.entity.User;
import com.wemo.backend.domain.user.repository.querydsl.UserQueryDsl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, UserQueryDsl {

    Optional<User> findByEmail(String username);

}
