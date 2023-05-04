package com.projectsassy.sassy.post.service;

import com.projectsassy.sassy.chatting.dto.NewestHomeDto;
import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.post.domain.Post;
import com.projectsassy.sassy.post.dto.CommentResponseDto;
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
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    //게시판 홈 조회순
    public List<ViewedHomeDto> findViewedPost(Pageable pageable) {

        Page<Post> allByViewed = postRepository.findAllByViewed(pageable);

        return allByViewed.stream().map(p -> new ViewedHomeDto(
                        p.getId(), p.getTitle(), p.getUser().getNickname(), p.getCategory(),
                        p.getCreatedAt().format(DateTimeFormatter.ofPattern("MM dd HH:mm"))))
                .collect(Collectors.toList());
    }

    //게시판 홈 최신순
    public List<NewestHomeDto> findNewestPost(Pageable pageable) {

        Page<Post> allByNewest = postRepository.findAllByNewest(pageable);

        return allByNewest.stream().map(p -> new NewestHomeDto(
                p.getId(), p.getTitle(), p.getUser().getNickname(), p.getCategory(),
                p.getCreatedAt().format(DateTimeFormatter.ofPattern("MM dd HH:mm"))
        )).collect(Collectors.toList());
    }

    // 게시글 작성
    @Transactional
    public Long createPost(CreatePostRequest createPostRequest) {
        User user = userService.findById(createPostRequest.getUserId());
        Post post = Post.of(createPostRequest, user);
        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }


    public LookUpPostResponse findPost(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> {throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_POST);
                });

        List<CommentResponseDto> commentResponseDtoList = findPost.getComments().stream()
                .map(c -> new CommentResponseDto(c)).collect(Collectors.toList());

        findPost.addPostCount();

        return new LookUpPostResponse(findPost, commentResponseDtoList);
    }


    // 게시글 찾기
    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_POST);
                });
    }
}
