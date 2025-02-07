package com.wemo.backend.domain.lightning.repository;

import com.wemo.backend.domain.lightning.entity.Lightning;
import com.wemo.backend.domain.lightning.repository.querydsl.LightningCursorQueryDsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightningRepository extends JpaRepository<Lightning, Long>, LightningCursorQueryDsl {

}
