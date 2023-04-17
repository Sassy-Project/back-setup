package com.projectsassy.sassy.user.dto;

import com.projectsassy.sassy.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinDto {

    @NotNull(message = "로그인 아이디는 Null 일 수 없습니다.")
    private String loginId;

    @NotNull(message = "비밀번호는 Null 일 수 없습니다.")
    private String password;

    @NotNull(message = "닉네임은 Null 일 수 없습니다.")
    private String nickname;

    @NotNull(message = "이메일은 Null 일 수 없습니다.")
    private String email;

    @NotNull(message = "성별은 Null 일 수 없습니다.")
    private String gender;

    @NotNull(message = "MBTI는 Null 일 수 없습니다.")
    private String mbti;

    public User toEntity() {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .nickname(nickname)
                .email(email)
                .gender(gender)
                .mbti(mbti)
                .image("defaultImage")
                .build();
    }
}
