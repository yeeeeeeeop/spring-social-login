package me.yeop.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenRequestDTO {

    private String refreshToken;

}
