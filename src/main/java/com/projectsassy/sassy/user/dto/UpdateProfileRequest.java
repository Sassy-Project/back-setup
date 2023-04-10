package com.projectsassy.sassy.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProfileRequest {

    @NotNull(message = "닉네임은 Null 일 수 없습니다.")
    private String nickname;

    @NotNull(message = "이메일은 Null 일 수 없습니다.")
    private String email;

    @NotNull(message = "MBTI는 Null 일 수 없습니다.")
    private String mbti;

}
