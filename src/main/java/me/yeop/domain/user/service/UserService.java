package me.yeop.domain.user.service;

import lombok.RequiredArgsConstructor;
import me.yeop.domain.user.dao.UserRepository;
import me.yeop.domain.user.domain.User;
import me.yeop.domain.user.dto.SignUpRequestDTO;
import me.yeop.domain.user.dto.SignUpResponseDTO;
import me.yeop.domain.user.factory.UserConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SignUpResponseDTO signup(SignUpRequestDTO request) {
        request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        User newUser = UserConverter.toEntityFromSignUpRequest(request);
        userRepository.save(newUser);

        return UserConverter.toSignUpResponseDTOFromEntity(newUser);
    }
}
