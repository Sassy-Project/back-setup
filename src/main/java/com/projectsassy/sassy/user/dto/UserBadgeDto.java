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
        this.badgeImage = "https://s3.ap-northeast-2.amazonaws.com/projectsassy.net/images/badge" + badge.getBadgeImage();
    }
}
