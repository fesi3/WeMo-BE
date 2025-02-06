package com.wemo.backend.domain.lightning.repository;

import com.wemo.backend.domain.lightning.entity.LightningType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightningTypeRepository extends JpaRepository<LightningType, Long> {
}
