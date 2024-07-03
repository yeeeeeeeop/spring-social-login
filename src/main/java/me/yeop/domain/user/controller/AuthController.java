package me.yeop.domain.user.controller;

import lombok.RequiredArgsConstructor;
import me.yeop.domain.user.dto.CreateAccessTokenRequestDTO;
import me.yeop.domain.user.dto.CreateAccessTokenResponseDTO;
import me.yeop.domain.user.dto.SignUpRequestDTO;
import me.yeop.domain.user.dto.SignUpResponseDTO;
import me.yeop.domain.user.service.TokenService;
import me.yeop.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signup(@RequestBody SignUpRequestDTO request) {
        SignUpResponseDTO response = userService.signup(request);
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/token")
    public ResponseEntity<CreateAccessTokenResponseDTO> createNewAccessToken(
            @RequestBody CreateAccessTokenRequestDTO request
    ) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponseDTO(newAccessToken));
    }
}
