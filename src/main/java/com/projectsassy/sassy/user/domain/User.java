package com.projectsassy.sassy.user.domain;

import lombok.*;

import javax.persistence.*;


@Entity @Getter
@Builder
@AllArgsConstructor
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String loginId;
    private String password;
    private String nickname;
    private String email;
    private String gender;
    private String mbti;
    private String image;

}
