package com.projectsassy.sassy.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class EmailRequest {
    @Email
    @NotBlank(message = "이메일을 입력하세요")
    private String email;
}
