package com.projectsassy.sassy.post.domain;

import com.projectsassy.sassy.post.dto.CreatePostRequest;
import com.projectsassy.sassy.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

    private Long count;

    @Enumerated(EnumType.STRING)
    private Category category;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Post(Long id, User user, List<Comment> comments, String title, Category category,
                String content, Long count, LocalDateTime createdAt) {
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
                .category(Category.valueOf(createPostRequest.getCategory()))
                .user(user)
                .build();
    }

    /**
     to Enum (나중에 String 값 변경)
     */
    public void toEnum(String categoryRequest) {
        if (categoryRequest.equals("자유게시판")) {
            this.category = Category.FREE;
        } else {
            this.category = Category.MBTI;
        }
    }


}
