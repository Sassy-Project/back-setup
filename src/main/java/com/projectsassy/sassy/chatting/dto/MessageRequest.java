package com.projectsassy.sassy.chatting.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRequest {

    private String type;
    private String roomId;
    private String sendUserId;
    private String content;

}
