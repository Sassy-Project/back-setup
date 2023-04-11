package com.projectsassy.sassy.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginRequest {

    @NotNull(message = "로그인 아이디는 Null 일 수 없습니다.")
    private String loginId;

    @NotNull(message = "비밀번호는 Null 일 수 없습니다.")
    private String password;

}
