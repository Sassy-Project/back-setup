package com.projectsassy.sassy.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ViewedHomeDto {

    private Long postId;
    private String title;
    private String category;
    private String nickName;
    private String writeTime;


}
