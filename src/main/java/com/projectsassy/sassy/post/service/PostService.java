package com.projectsassy.sassy.post.service;

import com.projectsassy.sassy.post.domain.Post;
import com.projectsassy.sassy.post.dto.HomeResponse;
import com.projectsassy.sassy.post.dto.CreatePostRequest;
import com.projectsassy.sassy.post.repository.PostRepository;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    //게시판 홈
    public List<HomeResponse> getPostHome(Integer limit, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, limit);

        Page<Post> postOfPage = postRepository.findAll(pageRequest);

        return postOfPage.stream().map(p -> new HomeResponse(
                        p.getId(), p.getTitle(), p.getUser().getNickname(), p.getCategory().toString(),
                        p.getCreatedAt().format(DateTimeFormatter.ofPattern("MM DD HH:mm"))))
                .collect(Collectors.toList());
    }

    // 게시글 작성
    public Long createPost(CreatePostRequest createPostRequest) {
        User user = userService.findById(createPostRequest.getUserId());

        Post post = Post.of(createPostRequest, user);

        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }
}
