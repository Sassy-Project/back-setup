package com.projectsassy.sassy.chatting.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchResponse {

    private String type;
    private String roomId;
    private String matchedUserNickname;

}
