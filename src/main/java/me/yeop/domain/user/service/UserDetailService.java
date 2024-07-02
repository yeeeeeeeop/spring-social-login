package me.yeop.domain.user.service;

import lombok.RequiredArgsConstructor;
import me.yeop.domain.user.dao.UserRepository;
import me.yeop.domain.user.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 사용자 이름(email)으로 사용자의 정보를 가져오는 메서드
     * 필수로 구현해야하는 메서드기에 오버라이딩해서 사용자 정보를 가져오는 로직을 작성함.
     * @param email {String} 사용자의 이메일
     * @return 사용자 {User}
     */
    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 이메일: " + email));
    }
}
