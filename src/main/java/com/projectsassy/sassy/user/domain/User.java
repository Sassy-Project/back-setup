package com.projectsassy.sassy.user.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;



@Entity @Getter
@Builder
@AllArgsConstructor
@Table(name = "USERS")
public class User {

    @Id @GeneratedValue
    private Long userId;

    private String loginId;
    private String password;
    private String nickname;
    private String email;
    private String gender;
    private String mbti;
    private String image;

}
