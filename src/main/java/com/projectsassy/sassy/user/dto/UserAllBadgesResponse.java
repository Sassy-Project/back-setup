package com.projectsassy.sassy.user.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UserAllBadgesResponse {

    private List<UserBadgeDto> userBadgeDtoList;

    public UserAllBadgesResponse(List<UserBadgeDto> userBadgeDtoList) {
        this.userBadgeDtoList = userBadgeDtoList;
    }
}
