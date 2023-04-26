package com.projectsassy.sassy.item.domain;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Badge")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Badge extends Item {

    private String badgeImage;

    public Badge(String name, int price, String badgeImage) {
        super(name, price);
        this.badgeImage = badgeImage;
    }
}
