package com.projectsassy.sassy.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePostRequest {

    private Long userId;
    private String title;
    private String content;
    private String category;



}
