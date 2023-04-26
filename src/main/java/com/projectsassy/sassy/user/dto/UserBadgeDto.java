package com.projectsassy.sassy.user.dto;

import com.projectsassy.sassy.item.domain.Badge;
import lombok.Getter;

@Getter
public class UserBadgeDto {

    private Long itemId;
    private String itemName;
    private String badgeImage;

    public UserBadgeDto(Badge badge) {
        this.itemId = badge.getId();
        this.itemName = badge.getName();
        this.badgeImage = badge.getBadgeImage();
    }
}
