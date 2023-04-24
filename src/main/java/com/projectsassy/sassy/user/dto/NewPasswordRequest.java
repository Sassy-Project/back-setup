package com.projectsassy.sassy.user.dto;

import lombok.Getter;

@Getter
public class NewPasswordRequest {

    private Long userId;
    private String newPassword;
    private String newPasswordCheck;
}
