package com.projectsassy.sassy.post.repository;

import com.projectsassy.sassy.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
