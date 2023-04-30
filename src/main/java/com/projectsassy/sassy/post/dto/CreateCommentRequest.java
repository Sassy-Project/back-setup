package com.projectsassy.sassy.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCommentRequest {

    private Long userId;
    private Long postId;
    private String comment;
}
