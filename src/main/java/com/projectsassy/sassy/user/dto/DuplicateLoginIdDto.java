package com.projectsassy.sassy.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateLoginIdDto {

    @NotNull(message = "로그인 아이디는 Null 일 수 없습니다.")
    private String loginId;
}
