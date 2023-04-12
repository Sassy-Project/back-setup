package com.projectsassy.sassy.chattingroom.domain;

import com.projectsassy.sassy.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CHATTINGROOM")
public class ChattingRoom {

    @Id @GeneratedValue
    @Column(name = "chattingroom_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "send_user_id")
    private User sendUser;

    @ManyToOne
    @JoinColumn(name = "receive_user_id")
    private User receiveUser;
    
}
