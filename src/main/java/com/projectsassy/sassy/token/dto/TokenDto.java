package com.projectsassy.sassy.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;
    private String grantType;
    private Long accessTokenExpiresIn;
}
