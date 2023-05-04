package com.projectsassy.sassy.post.domain;

import com.projectsassy.sassy.post.dto.CreatePostRequest;
import com.projectsassy.sassy.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "POST")
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    private String title;
    private String content;

    private int count;

    private String category;

    @CreatedDate
    @LastModifiedDate
    private LocalDateTime createdAt;

    @Builder
    public Post(Long id, User user, List<Comment> comments, String title, String category,
                String content, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.comments = comments;
        this.title = title;
        this.category = category;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static Post of(CreatePostRequest createPostRequest, User user) {
        return Post.builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .category(createPostRequest.getCategory())
                .user(user)
                .build();
    }


    public void addPostCount() {
        this.count++;
    }
}
