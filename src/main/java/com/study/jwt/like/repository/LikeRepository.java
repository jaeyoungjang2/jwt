package com.study.jwt.like.repository;

import com.study.jwt.account.entity.Account;
import com.study.jwt.like.entity.Like;
import com.study.jwt.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndAccount(Post post, Account account);

    boolean existsByPostAndAccount(Post post, Account account);

    void deleteByPostAndAccount(Post post, Account account);
}
