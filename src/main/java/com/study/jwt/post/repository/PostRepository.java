package com.study.jwt.post.repository;

import com.study.jwt.post.service.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
