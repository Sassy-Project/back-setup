package com.projectsassy.sassy.websoket;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRequest {

    private String type;
    private String sendUserId;
    private String roomId;
    private String content;

}
