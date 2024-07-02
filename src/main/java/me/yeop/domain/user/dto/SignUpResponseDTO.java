package me.yeop.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignUpResponseDTO {

    private Long userId;

    private String email;

}
