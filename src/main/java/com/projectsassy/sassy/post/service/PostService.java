package com.projectsassy.sassy.post.service;

import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.post.domain.Post;
import com.projectsassy.sassy.post.dto.ViewedHomeDto;
import com.projectsassy.sassy.post.dto.CreatePostRequest;
import com.projectsassy.sassy.post.dto.LookUpPostResponse;
import com.projectsassy.sassy.post.repository.PostRepository;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    //게시판 홈
    public List<ViewedHomeDto> findViewedPost(Pageable pageable) {

        Page<Post> postOfPage = postRepository.findAllByViewed(pageable);

        return postOfPage.stream().map(p -> new ViewedHomeDto(
                        p.getId(), p.getTitle(), p.getUser().getNickname(), p.getCategory(),
                        p.getCreatedAt().format(DateTimeFormatter.ofPattern("MM DD HH:mm"))))
                .collect(Collectors.toList());
    }

    // 게시글 작성
    @Transactional
    public Long createPost(CreatePostRequest createPostRequest) {
        User user = userService.findById(createPostRequest.getUserId());
        Post post = Post.of(createPostRequest, user);
        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }


    @Transactional
    public LookUpPostResponse findPost(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> {throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_POST);
                });

        LookUpPostResponse lookUpPostResponse = new LookUpPostResponse(
                findPost.getUser().getNickname(), findPost.getTitle(), findPost.getContent(),
                findPost.getCategory());

        findPost.addPostCount();

        return lookUpPostResponse;
    }
}
