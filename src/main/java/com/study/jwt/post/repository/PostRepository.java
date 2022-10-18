package com.study.jwt.post.repository;

import com.study.jwt.account.entity.Account;
import com.study.jwt.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
