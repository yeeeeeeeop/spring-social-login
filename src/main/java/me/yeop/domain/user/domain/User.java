package me.yeop.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;
import me.yeop.domain.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users") // 어떤 테이블에 매핑될지 지정하는 애너테이션
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Builder
    public User(String email, String password, String auth) {
        this.email = email;
        this.password = password;
    }

    /**
     * 사용자가 가지고 있는 권한의 목록을 반환한다.
     * 현재 예제 코드에서는 사용자 이외의 권한이 없기 때문에 user 권한만 담아 반환한다.
     * @return 유저 권한 {Collection<? extends GrantedAuthority>}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    /**
     * 사용자를 식별할 수 있는 사용자 이름을 반환한다.
     * 이때 사용되는 사용자 이름은 반드시 고유해야 한다.
     * 현재 예제 코드는 유니크 속성이 적용된 이메일을 반환한다.
     * @return 이메일 {String}
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 사용자의 비밀번호를 반환한다.
     * 이때 저장되어 있는 비밀번호는 암호화해서 저장해야한다.
     * @return 비밀번호 {String}
     */
    @Override // 사용자의 패스워드 반환
    public String getPassword() {
        return password;
    }

    /**
     * 계정이 만료되었는지 확인하는 메서드이다.
     * 만약 만료되지 않은 때는 true를 반환한다.
     * @return 계정 만료 여부 {boolean}
     */
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    /**
     * 계정이 잠금되었는지 확인하는 메서드이다.
     * 만약 잠금되지 않은 때는 true를 반환한다.
     * @return 계정 잠금 여부 {true}
     */
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }

    /**
     * 비밀번호가 만료되었는지 확인하는 메서드이다.
     * 만약 만료되지 않은 때는 true를 반환한다.
     * @return 비밀번호 만료 여부 {boolean}
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    /**
     * 계정이 사용 가능한지 확인하는 메서드이다.
     * 만약 사용 가능하다면 true를 반환한다.
     * @return 계정 사용 가능 여부 {boolean}
     */
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }
}
