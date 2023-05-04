package com.projectsassy.sassy.chatting.dto;

import lombok.Getter;

@Getter
public class MbtiRecommendList {

    private String mbti;
    private Long count;

    public MbtiRecommendList(String mbti, Long count) {
        this.mbti = mbti;
        this.count = count;
    }
}
