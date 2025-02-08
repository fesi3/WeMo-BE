package com.wemo.backend.domain.lightningJoin.repository;

import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightningJoin.entity.LightningJoin;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LightningJoinRepository extends JpaRepository<LightningJoin, Long> {

    boolean existsByUserAndLightning(User user, Lightning lightning);

    long countByLightning(Lightning lightning);

    List<LightningJoin> findAllByLightning(Lightning lightning);

    void deleteAllByLightning(Lightning lightning);

}
