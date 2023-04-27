package com.projectsassy.sassy.userItem.domain;

import com.projectsassy.sassy.item.domain.Item;
import com.projectsassy.sassy.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserItem {

    @Id
    @GeneratedValue
    @Column(name = "user_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private UserItem(Item item) {
        this.item = item;
    }

    public static UserItem createUserItem(Item item) {
        UserItem userItem = new UserItem(item);
        return userItem;
    }

    public void addUser(User user) {
        this.user = user;
    }
}
