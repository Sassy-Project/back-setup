package com.projectsassy.sassy.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailRequest {
    @NotBlank(message = "이메일을 입력하세요")
    private String email;
}
