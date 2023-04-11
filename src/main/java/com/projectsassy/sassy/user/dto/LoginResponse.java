package com.projectsassy.sassy.user.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private Long id;
    private String nickname;

    public LoginResponse(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
