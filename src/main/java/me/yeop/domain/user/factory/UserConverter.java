package me.yeop.domain.user.factory;

import lombok.RequiredArgsConstructor;
import me.yeop.domain.user.domain.User;
import me.yeop.domain.user.dto.SignUpRequestDTO;
import me.yeop.domain.user.dto.SignUpResponseDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class UserConverter {

    public static User toEntityFromSignUpRequest(SignUpRequestDTO request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public static SignUpResponseDTO toSignUpResponseDTOFromEntity(User user) {
        return SignUpResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }
}
