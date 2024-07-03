package me.yeop.global.config.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.yeop.domain.user.dao.RefreshTokenRepository;
import me.yeop.domain.user.domain.RefreshToken;
import me.yeop.domain.user.domain.User;
import me.yeop.domain.user.service.UserService;
import me.yeop.global.config.jwt.JwtTokenProvider;
import me.yeop.global.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final int REFRESH_TOKEN_DAYS_EXPIRY = 14;
    public static final int ACCESS_TOKEN_DAYS_EXPIRY = 1;
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(REFRESH_TOKEN_DAYS_EXPIRY);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(ACCESS_TOKEN_DAYS_EXPIRY);

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail((String) oAuth2User.getAttributes().get("email"));

        // 리프레시 토큰 생성 -> 저장 -> 쿠키에 저장
        String refreshToken = jwtTokenProvider.generateUserToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        // 액세스 토큰 생성 -> 패스에 액세스 토큰 추가
        String accessToken = jwtTokenProvider.generateUserToken(user, ACCESS_TOKEN_DURATION);
//        String targetUrl = getTargetUrl(accessToken);

        // 인증 관련된 설정값, 쿠키 제거
        clearAuthenticationAttributes(request, response);

        // 리다이렉트(생략)
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * 생성된 리프레시 토큰을 전달받아 데이터베이스에 저장
     *
     * @param userId          유저 ID
     * @param newRefreshToken 새로운 refreshToken
     */
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    /**
     * 생성된 리프레시 토큰을 쿠키에 저장
     *
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     * @param refreshToken refreshToken
     */
    private void addRefreshTokenToCookie(
            HttpServletRequest request,
            HttpServletResponse response,
            String refreshToken
    ) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toMillis();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    /**
     * 인증 관련 설정값, 쿠키 제거
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    private void clearAuthenticationAttributes(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}
