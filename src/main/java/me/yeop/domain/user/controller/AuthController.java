package me.yeop.domain.user.controller;

import lombok.RequiredArgsConstructor;
import me.yeop.domain.user.dto.SignUpRequestDTO;
import me.yeop.domain.user.dto.SignUpResponseDTO;
import me.yeop.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signup(@RequestBody SignUpRequestDTO request) {
        SignUpResponseDTO response = userService.signup(request);
        return ResponseEntity.ok()
                .body(response);
    }
}
