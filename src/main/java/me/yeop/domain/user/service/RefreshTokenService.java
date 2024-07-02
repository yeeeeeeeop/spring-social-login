package me.yeop.domain.user.service;

import lombok.RequiredArgsConstructor;
import me.yeop.domain.user.dao.RefreshTokenRepository;
import me.yeop.domain.user.domain.RefreshToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 토큰입니다: " + refreshToken));
    }
}
