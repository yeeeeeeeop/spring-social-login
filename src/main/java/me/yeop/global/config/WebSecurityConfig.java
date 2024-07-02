package me.yeop.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 해당 클래스가 하나 이상의 @Bean 메서드를 정의하고, 스프링 IoC 컨테이너에서 빈 정의를 생성하고 서비스 요청을 처리할 것을 나타냄.
@EnableWebSecurity // 스프링 시큐리티의 웹 보안 지원을 활성화하는데 사용
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    /**
     * 특정 HTTP 요청에 대한 웹 기반 보안 구성
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> auth
                        .requestMatchers( // 특정 요청과 일치하는 url에 대한 액세스를 설정한다.
                                new AntPathRequestMatcher("/login"), // URL 패턴 매칭을 수행하는 데 사용되는 클래스
                                new AntPathRequestMatcher("/signup"),
                                new AntPathRequestMatcher("/user")
                        ).permitAll() // 해당 url로 요청이 오면 인증/인가 없이도 접근할 수 있다.
                        .anyRequest() // 위에서 설정한 url 이외의 요청에 대해서 설정
                        .authenticated()) // 별도의 인가는 필요하지 않지만 인증이 성공된 상태여야 접근 가능.
                .logout(logout -> logout
                        .invalidateHttpSession(true)) // 로그아웃 이후에 세션을 전체 삭제할지 여부 결정
                .csrf(AbstractHttpConfigurer::disable) // CSRF 공격을 방지하기 위해서는 활성화하지만, 실습의 편의를 위해 비활성화
                .build();
    }

    /**
     * 인증 관리자 관련 설정.
     * 사용자 정보를 가져올 서비스를 재정의하거나,
     * 인증방법, 예를 들어 LDAP, JDBC 기반 인증 등을 설정할 때 사용한다.
     * @param http
     * @param bCryptPasswordEncoder
     * @param userDetailsService
     * @return ProviderManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserDetailsService userDetailsService) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // 사용자 정보를 가져올 서비스를 설정한다. 반드시 UserDetailsService를 상속한 클래스여야 한다.
        authProvider.setPasswordEncoder(bCryptPasswordEncoder); // 비밀번호를 암호화하기 위한 인코더를 설정한다.
        return new ProviderManager(authProvider);
    }

    /**
     * 패스워드 인코더로 사용할 빈 등록
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
