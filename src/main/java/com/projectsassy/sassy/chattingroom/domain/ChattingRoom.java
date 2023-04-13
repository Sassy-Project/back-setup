package com.projectsassy.sassy.chattingroom.domain;

import com.projectsassy.sassy.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ROOM")
public class ChattingRoom {

    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_user_id")
    private User sendUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_user_id")
    private User receiveUser;

    private String wantMbti;

    @Builder
    private ChattingRoom(User sendUser, String wantMbti) {
        this.sendUser = sendUser;
        this.wantMbti = wantMbti;
    }

    public static ChattingRoom of(User sendUser, String wantMbti) {
        return ChattingRoom.builder()
            .sendUser(sendUser)
            .wantMbti(wantMbti)
            .build();
    }

    public void enterUser(User receiveUser) {
        this.receiveUser = receiveUser;
    }
}
