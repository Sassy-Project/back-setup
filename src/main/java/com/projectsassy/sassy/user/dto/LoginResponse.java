package com.projectsassy.sassy.user.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private String nickname;

    public LoginResponse(String nickname) {
        this.nickname = nickname;
    }
}
