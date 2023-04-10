package com.projectsassy.sassy.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdatePasswordRequest {

    @NotNull(message = "기존 비밀번호는 Null 일 수 없습니다.")
    private String password;

    @NotNull(message = "새로운 비밀번호는 Null 일 수 없습니다.")
    private String updatePassword;

}
