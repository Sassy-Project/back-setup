package com.projectsassy.sassy.user.dto;

import lombok.Getter;

@Getter
public class UpdateProfileResponse {

    private String nickname;

    public UpdateProfileResponse(String nickname) {
        this.nickname = nickname;
    }
}
