package com.projectsassy.sassy.chatting.dto;

import lombok.Getter;

@Getter
public class ChatCloseResponse {

    private String type;
    private String roomId;
    private String message;

    public ChatCloseResponse(Long roomId) {
        this.type = "close";
        this.roomId = String.valueOf(roomId);
        this.message = "연결이 종료되었습니다.";
    }
}
