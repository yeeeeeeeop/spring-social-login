package me.yeop.global.config.oauth;

import lombok.RequiredArgsConstructor;
import me.yeop.domain.user.dao.UserRepository;
import me.yeop.domain.user.domain.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    /**
     * 요청을 바탕으로 유저 정보를 담은 객체 반환
     *
     * @param userRequest OAuth2UserRequest
     * @return OAuth2User
     * @throws OAuth2AuthenticationException OAuth2 인증 에러
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);
        return user;
    }

    /**
     * 유저가 있으면 업데이트, 없으면 유저 생성
     *
     * @param oAuth2User OAuth2User
     * @return 업데이트 혹은 생성된 User
     */
    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .nickname(name)
                        .build());

        return userRepository.save(user);
    }
}
