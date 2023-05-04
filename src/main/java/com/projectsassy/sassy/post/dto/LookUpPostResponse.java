package com.projectsassy.sassy.post.dto;

import com.projectsassy.sassy.post.domain.Comment;
import com.projectsassy.sassy.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class LookUpPostResponse {

    private String nickname;
    private String title;
    private String content;
    private String category;
    private String createAt;

    private List<CommentResponseDto> commentList;

    public LookUpPostResponse(Post post, List<CommentResponseDto> commentResponseDtoList) {
        this.nickname = post.getUser().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.createAt = post.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.commentList = commentResponseDtoList;
    }
}
