package com.projectsassy.sassy.user.dto;

import com.projectsassy.sassy.token.dto.TokenResponse;
import lombok.Getter;

@Getter
public class LoginResponse {

    private Long id;
    private String nickname;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;

    public LoginResponse(Long id, String nickname, TokenResponse tokenResponse) {
        this.id = id;
        this.nickname = nickname;
        this.accessToken = tokenResponse.getAccessToken();
        this.refreshToken = tokenResponse. getRefreshToken();
        this.accessTokenExpiresIn = tokenResponse.getAccessTokenExpiresIn();
    }
}
