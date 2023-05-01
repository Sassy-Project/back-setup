package com.projectsassy.sassy.item.dto;

import com.projectsassy.sassy.item.domain.Badge;
import lombok.Getter;

@Getter
public class BadgeDto {

    private Long itemId;
    private String badgeName;
    private int price;
    private String badgeImage;

    public BadgeDto(Badge badge) {
        this.itemId = badge.getId();
        this.badgeName = badge.getName();
        this.price = badge.getPrice();
        this.badgeImage = "https://s3.ap-northeast-2.amazonaws.com/projectsassy.net/images/badge" + badge.getBadgeImage();
    }
}
