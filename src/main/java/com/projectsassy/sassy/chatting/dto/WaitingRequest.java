package com.projectsassy.sassy.chatting.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingRequest {

    private String type;
    private String userId;
    private String selectMbti;

}
