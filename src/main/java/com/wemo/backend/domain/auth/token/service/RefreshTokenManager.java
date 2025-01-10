package com.wemo.backend.domain.auth.token.service;

import com.wemo.backend.domain.auth.token.entity.RefreshToken;
import com.wemo.backend.domain.auth.token.repository.RefreshTokenRepository;
import com.wemo.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.wemo.backend.global.exception.ErrorCode.ILLEGAL_REFRESH_TOKEN_NOT_VALID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenManager {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * JWT refreshToken 생성 및 저장
     *
     * @param email 유저 아이디
     * @return 저장된 refreshToken
     */
    @Transactional
    public String saveJwtRefreshToken(final String email) {

        String refreshToken = jwtTokenUtils.generateRefreshToken(email);

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByEmail(email);

        if (existingToken.isPresent()) {
            refreshTokenRepository.delete(existingToken.get());
            log.info("기존에 존재하는 refreshToken 삭제 완료");
        }

        refreshTokenRepository.save(new RefreshToken(refreshToken, email));

        return refreshToken;
    }

    /**
     * refreshToken 유효성 검증
     *
     * @param refreshToken jwt 형태의 refreshToken
     * @return 검증이 완료된 refreshToken 객체 반환
     */
    public RefreshToken getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByEmail(refreshToken).orElseThrow(
                () -> new CustomException(ILLEGAL_REFRESH_TOKEN_NOT_VALID)
        );

    }

}
