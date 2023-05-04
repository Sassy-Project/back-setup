package com.projectsassy.sassy.post.service;

import com.projectsassy.sassy.post.domain.Comment;
import com.projectsassy.sassy.post.domain.Post;
import com.projectsassy.sassy.post.dto.CreateCommentRequest;
import com.projectsassy.sassy.post.repository.CommentRepository;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    
    public Long createComment(CreateCommentRequest createCommentRequest) {
        Post post = postService.findById(createCommentRequest.getPostId());
        User user = userService.findById(createCommentRequest.getUserId());

        Comment comment = Comment.of(createCommentRequest, user, post);
        commentRepository.save(comment);

        return comment.getId();
    }
}
