package com.projectsassy.sassy.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MessageResponse {

    private String type;
    private String roomId;
    private String sendUserId;
    private String content;
    private String time;
    private String nickname;

    public MessageResponse(Long roomId, Long sendUserId, String content, String time, String nickname) {
        this.type = "chat";
        this.roomId = String.valueOf(roomId);
        this.sendUserId = String.valueOf(sendUserId);
        this.content = content;
        this.time = time;
        this.nickname = nickname;
    }
}
