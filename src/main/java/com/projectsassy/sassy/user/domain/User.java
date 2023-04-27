package com.projectsassy.sassy.user.domain;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.BusinessExceptionHandler;
import com.projectsassy.sassy.userItem.domain.UserItem;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
    private String gender;
    private String mbti;
    private String image;
    private int point;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserItem> userItems = new ArrayList<>();

    @Embedded
    private Email email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    private User(String loginId, String password, String nickname, String email, String gender, String mbti, String image) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.email = new Email(email);
        this.gender = gender;
        this.mbti = mbti;
        this.image = image;
        this.role = Role.ROLE_USER;
        this.point = 0;
    }

    public static User of(String loginId, String password, String nickname, String email, String gender, String mbti, String image) {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .nickname(nickname)
                .email(email)
                .gender(gender)
                .mbti(mbti)
                .image(image)
                .build();
    }

    //      패스워드 인코딩
    public void encodingPassword(String password) {
        this.password = password;
    }

    public void updateProfile(String updatedNickname, String updatedEmail, String updatedMbti, String updateGender) {
        this.nickname = updatedNickname;
        this.email = new Email(updatedEmail);
        this.mbti = updatedMbti;
        this.gender = updateGender;
    }

    public void changePassword(String updatePassword) {
        this.password = updatePassword;
    }

    public void addPoint() {
        this.point += 10;
    }

    public void addUserItem(UserItem userItem) {
        this.userItems.add(userItem);
        userItem.addUser(this);
    }

    public void purchaseItem(UserItem userItem) {
        if (this.point >= userItem.getItem().getPrice()) {
            this.point -= userItem.getItem().getPrice();
            this.addUserItem(userItem);
            return;
        }

        throw new BusinessExceptionHandler(ErrorCode.INSUFFICIENT_POINTS);
    }

    public void changeImage(String badgeImage) {
        this.image = badgeImage;
    }
}
