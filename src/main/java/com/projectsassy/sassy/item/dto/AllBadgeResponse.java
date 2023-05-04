package com.projectsassy.sassy.item.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AllBadgeResponse {

    private List<BadgeDto> badgeDtos;

    public AllBadgeResponse(List<BadgeDto> badgeDtos) {
        this.badgeDtos = badgeDtos;
    }
}
