package com.projectsassy.sassy.user.dto;

import com.projectsassy.sassy.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinDto {

    @NotNull
    private String loginId;

    @NotNull
    private String password;

    @NotNull
    private String nickname;

    @NotNull
    private String email;

    @NotNull
    private String gender;

    @NotNull
    private String mbti;

    @NotNull
    private String image;

    public User toEntity() {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .nickname(nickname)
                .email(email)
                .gender(gender)
                .mbti(mbti)
                .image(image)
                .build();
    }
}
