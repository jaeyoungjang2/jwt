package com.study.jwt.like.repository;

import com.study.jwt.account.entity.Account;
import com.study.jwt.like.entity.Like;
import com.study.jwt.post.service.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndAccount(Post post, Account account);
}
