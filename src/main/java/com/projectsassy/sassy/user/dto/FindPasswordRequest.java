package com.projectsassy.sassy.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FindPasswordRequest {

    @NotNull
    private String email;

    @NotNull
    private String loginId;

    @NotNull
    private String code;
}
