package com.projectsassy.sassy.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPasswordDto {

    private String email;
    private String loginId;
    private String code;
}
