package com.projectsassy.sassy.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateLoginIdDto {

    @NotNull
    private String loginId;
}
