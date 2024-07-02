package me.yeop.global.config.jwt;

import io.jsonwebtoken.Jwts;
import me.yeop.domain.user.dao.UserRepository;
import me.yeop.domain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProperties jwtProperties;

    @Test
    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    void generateToken() {
        // given
        User testUser = userRepository.save(User.builder()
                        .email("user@email.com")
                        .password("test")
                .build());

        // when
        String token = jwtTokenProvider.generateUserToken(testUser, Duration.ofDays(14));

        // then
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("validateToken(): 만료된 토큰인 때에 유효성 검증에 실패한다.")
    void validateToken_invalidToken() {
        // given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        // when
        boolean result = jwtTokenProvider.validateToken(token);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("validateToken(): 유효한 토큰인 때에 유효성 검증에 성공한다.")
    void validateToken_validToken() {
        // given
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        // when
        boolean result = jwtTokenProvider.validateToken(token);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
    void getAuthentication() {
        // given
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        // when
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        // then
        assertThat(((UserDetails)authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    @Test
    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
    void getUserId() {
        // given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        // when
        Long userIdByToken = jwtTokenProvider.getUserId(token);

        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}
