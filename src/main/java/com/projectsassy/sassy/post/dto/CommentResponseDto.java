package com.projectsassy.sassy.post.dto;

import com.projectsassy.sassy.post.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto {

    private String nickname;
    private String comment;
    private String createAt;

    public CommentResponseDto(Comment comment) {
        this.nickname = comment.getUser().getNickname();
        this.comment = comment.getComment();
        this.createAt = comment.getCreatedAt().format(
                DateTimeFormatter.ofPattern("dd HH:mm"));
    }
}
