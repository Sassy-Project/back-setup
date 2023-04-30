package com.projectsassy.sassy.post.domain;

import com.projectsassy.sassy.post.dto.CreateCommentRequest;
import com.projectsassy.sassy.post.dto.CreatePostRequest;
import com.projectsassy.sassy.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "COMMENT")
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String comment;

    @CreatedDate
    @LastModifiedDate
    private LocalDateTime createdAt;

    @Builder
    public Comment(Long id, Post post, User user, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.post = post;
        this.user = user;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public static Comment of(CreateCommentRequest createCommentRequest, User user, Post post) {
        return Comment.builder()
                .comment(createCommentRequest.getComment())
                .post(post)
                .user(user)
                .build();
    }
}
