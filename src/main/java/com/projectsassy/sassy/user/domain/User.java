package com.projectsassy.sassy.user.domain;

import lombok.*;

import javax.persistence.*;

import static lombok.AccessLevel.*;


@Entity @Getter
@NoArgsConstructor(access = PROTECTED)
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

    @Builder
    public User(Long id, String loginId, String password, String nickname, String email
            , String gender, String mbti, String image) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.mbti = mbti;
        this.image = image;
    }

    public static User of(String loginId, String password, String nickname
            , String email, String gender, String mbti, String image) {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .nickname(nickname)
                .email(email)
                .gender(gender)
                .mbti(mbti)
                .image(image).build();
    }

    //      패스워드 인코딩
    public void encodingPassword(String password) {
        this.password = password;
    }
}
