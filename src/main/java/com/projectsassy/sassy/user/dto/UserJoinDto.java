package com.projectsassy.sassy.user.dto;

import com.projectsassy.sassy.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserJoinDto {

    private String loginId;
    private String password;
    private String nickname;
    private String email;
    private String gender;
    private String mbti;
    private String image;

    public User toEntity() {
        return User.of(loginId, password, nickname, email, gender, mbti, image);
    }
}
