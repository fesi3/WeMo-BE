package com.wemo.backend.domain.lightningJoin.repository;

import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightningJoin.entity.LightningJoin;
import com.wemo.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightningJoinRepository extends JpaRepository<LightningJoin, Long> {

    boolean existsByUserAndLightning(User user, Lightning lightning);

    long countByLightning(Lightning lightning);

    void deleteAllByLightning(Lightning lightning);

    void deleteByUserAndLightning(User user, Lightning lightning);

}
