package com.wemo.backend.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.wemo.backend.domain.image.dto.PresignedUrlResponse;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.wemo.backend.global.exception.ErrorCode.IMAGE_COUNT_EXCEEDED;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${presigned.url.expiration.time}")
    private long presignedUrlExpirationTime;

    private final AmazonS3 s3Client;

    /**
     * presignedUrl 생성
     *
     * @param count 필요한 url 개수
     * @return 생성된 presignedUrl 리스트
     */
    public PresignedUrlResponse generatePresignedUrl(int count) {

        log.info("이미지 업로드를 위한 presignedUrl 요청 메서드 호출");

        if (count > 10) throw new CustomException(IMAGE_COUNT_EXCEEDED);

        // presigned URL 목록 생성
        List<String> presignedUrls = IntStream.range(0, count)  // count 만큼 URL 생성
                .mapToObj(i -> {
                    String uniqueKey = generateUniqueKey();

                    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, uniqueKey)
                            .withMethod(com.amazonaws.HttpMethod.PUT)
                            .withExpiration(new Date(System.currentTimeMillis() + presignedUrlExpirationTime));

                    URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

                    return url.toString();
                })
                .collect(Collectors.toList());

        log.info("{}개의 presignedUrl List 생성", count);

        // presignedUrl을 PresignedUrlResponse에 담아서 반환
        return PresignedUrlResponse.builder()
                .presignedUrl(presignedUrls)  // List<String> 반환
                .build();
    }

    // 랜덤한 고유 키값 생성
    private String generateUniqueKey() {

        return UUID.randomUUID() + "-" + System.currentTimeMillis();
    }

}
