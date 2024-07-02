package me.yeop.domain.user.service;

import lombok.RequiredArgsConstructor;
import me.yeop.domain.user.domain.User;
import me.yeop.global.config.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final static int ACCESS_TOKEN_EXPIRY_HOURS = 2;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다: " + refreshToken);
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return jwtTokenProvider.generateUserToken(user, Duration.ofHours(ACCESS_TOKEN_EXPIRY_HOURS));
    }
}
