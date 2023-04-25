package com.projectsassy.sassy.item.domain;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Badge")
@Getter
public class Badge extends Item {

    private String badgeImage;
}
