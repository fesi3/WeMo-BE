package com.wemo.backend.domain.region.service;

import com.wemo.backend.global.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.wemo.backend.global.exception.ErrorCode.ILLEGAL_ADDRESS_NOT_VALID;

@Service
public class RegionServiceImpl {

    public static Map<String, String> parseAddress(String address) {

        String[] parts = address.split(" ");
        if (parts.length < 2) {
            throw new CustomException(ILLEGAL_ADDRESS_NOT_VALID);
        }

        Map<String, String> addressMap = new HashMap<>();
        addressMap.put("province", parts[0]); // 시/도
        addressMap.put("district", parts[1]); // 군/구
        return addressMap;
    }

}
