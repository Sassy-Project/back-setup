package com.projectsassy.sassy.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class UpdateProfileResponse {

    private String nickname;
    private String email;
    private String mbti;
    private String gender;

    public UpdateProfileResponse(String nickname, String email, String mbti, String gender) {
        this.nickname = nickname;
        this.email = email;
        this.mbti = mbti;
        this.gender = gender;
    }
}
