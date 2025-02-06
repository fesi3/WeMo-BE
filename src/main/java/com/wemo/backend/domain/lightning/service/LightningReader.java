package com.wemo.backend.domain.lightning.service;


import com.wemo.backend.domain.lightning.entity.Lightning;

public interface LightningReader {
    
    Lightning getLightningById(Long lightningId);

}
