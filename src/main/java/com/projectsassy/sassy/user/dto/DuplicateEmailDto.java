package com.projectsassy.sassy.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateEmailDto {

    @NotNull(message = "이메일은 Null 일 수 없습니다.")
    private String email;
}
