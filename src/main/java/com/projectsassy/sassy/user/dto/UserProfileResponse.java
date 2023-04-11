package com.projectsassy.sassy.user.dto;

import com.projectsassy.sassy.user.domain.User;
import lombok.Getter;

@Getter
public class UserProfileResponse {

    private String loginId;
    private String nickname;
    private String email;
    private String mbti;

    public UserProfileResponse(User user) {
        this.loginId = user.getLoginId();
        this.nickname = user.getNickname();
        this.email = user.getEmail().getEmail();
        this.mbti = user.getMbti();
    }

}
