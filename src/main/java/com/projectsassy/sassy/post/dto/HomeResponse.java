package com.projectsassy.sassy.post.dto;

import com.projectsassy.sassy.user.domain.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HomeResponse {

    private Long postId;
    private String title;
    private String category;
    private String nickName;
    private String writeTime;


}
