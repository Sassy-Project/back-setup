package com.projectsassy.sassy.post.controller;

import com.projectsassy.sassy.post.dto.CreateCommentRequest;
import com.projectsassy.sassy.post.dto.CreateCommentResponse;
import com.projectsassy.sassy.post.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @ApiOperation("댓글 작성")
    @PostMapping("/write")
    public ResponseEntity createComment(@RequestBody CreateCommentRequest createCommentRequest) {

        Long commentId = commentService.createComment(createCommentRequest);
        CreateCommentResponse createCommentResponse = new CreateCommentResponse(commentId);

        return new ResponseEntity(createCommentResponse, HttpStatus.OK);
    }

}
