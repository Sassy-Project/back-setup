package com.projectsassy.sassy.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewestHomeDto {

    private Long postId;
    private String title;
    private String category;
    private String nickName;
    private String writeTime;
}
