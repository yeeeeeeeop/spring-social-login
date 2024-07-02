package me.yeop.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.yeop.domain.user.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    /**
     * 유저의 토큰 발급
     *
     * @param user      유저
     * @param expiredAt 만료시간
     * @return JWT 토큰
     */
    private String generateUserToken(User user, Duration expiredAt) {
        Date now = new Date();
        return createUserToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    /**
     * 유저의 JWT 토큰 생성
     *
     * @param expiry 만료 시간
     * @param user   유저
     * @return JWT 토큰
     */
    private String createUserToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                // 헤더(header)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // typ: JWT
                // 내용(payload)
                .setIssuer(jwtProperties.getIssuer()) // iss: issuer
                .setIssuedAt(now) // iat: 발행 시간 현재
                .setExpiration(expiry) // exp: 만료 시간
                .setSubject(user.getEmail()) // sub: 제목(유저 이메일)
                .claim("id", user.getId()) // 클레임 id: 유저 ID
                // 서명(signature)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    /**
     * 토큰의 유효성 검증
     *
     * @param token JWT 토큰
     * @return 토큰 유효 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀키로 복호화
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false; // 복호화 과정에서 에러가 나면 유효하지 않은 토큰
        }
    }

    /**
     * 토큰 기반으로 인증 정보를 가져오는 메서드
     *
     * @param token JWT 토큰
     * @return 인증 정보
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(
                // 프로젝트에서 만든 User 클래스가 아닌 스프링 시큐리티에서 제공하는 User 클래스 임포트 필요!
                new org.springframework.security.core.userdetails.User(
                        claims.getSubject(),
                        "",
                        authorities),
                token,
                authorities
        );
    }

    /**
     * 토큰 기반으로 유저 ID를 가져오는 메서드
     *
     * @param token JWT 토큰
     * @return User ID
     */
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    /**
     * 토큰을 파싱하여 클레임 조회
     *
     * @param token JWT 토큰
     * @return 토큰 클레임
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
