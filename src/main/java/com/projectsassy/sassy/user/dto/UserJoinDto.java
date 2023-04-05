package com.projectsassy.sassy.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserJoinDto {

    private String loginId;
    private String password;
    private String passwordCheck;
    private String nickname;
    private String email;
    private String gender;
    private String mbti;
    private String image;
}
