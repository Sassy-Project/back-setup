package com.projectsassy.sassy.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomInformation {

    private Long roomId;
    private String matchedUserNickname;

}
