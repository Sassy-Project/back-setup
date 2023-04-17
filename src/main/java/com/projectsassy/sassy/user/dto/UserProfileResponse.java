package com.projectsassy.sassy.user.dto;

import com.projectsassy.sassy.user.domain.User;
import lombok.Getter;

@Getter
public class UserProfileResponse {

    private String loginId;
    private String nickname;
    private String email;
    private String mbti;
    private String gender;

    public UserProfileResponse(String loginId, String nickname, String email, String mbti, String gender) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.email = email;
        this.mbti = mbti;
        this.gender = gender;
    }
}
