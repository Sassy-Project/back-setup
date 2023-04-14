package com.projectsassy.sassy.websoket.second;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingRequest {

    private String userId;
    private String selectMbti;

}
