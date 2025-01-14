package com.wemo.backend.domain.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PresignedUrlResponse {

    private List<String> presignedUrl;

}
