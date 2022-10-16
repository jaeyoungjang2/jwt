package com.study.jwt.like.service;

import com.study.jwt.account.entity.Account;
import com.study.jwt.account.repository.AccountRepository;
import com.study.jwt.like.dto.LikeRequestDto;
import com.study.jwt.like.entity.Like;
import com.study.jwt.like.repository.LikeRepository;
import com.study.jwt.post.repository.PostRepository;
import com.study.jwt.post.service.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final LikeRepository likeRepository;

    public boolean addLikeOrDeleteLike(Account account, LikeRequestDto likeRequestDto) {
        Post post = postRepository.findById(likeRequestDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );

        // 좋아요를 누른 상태인지, 누르지 않은 상태인지 확인할 필요가 있습니다.
        Optional<Like> foundLike = likeRepository.findByPostAndAccount(post, account);
        if (foundLike.isPresent()) {
            likeRepository.delete(foundLike.get());
            return false;
        } else {
            Like like = new Like(account, post);
            likeRepository.save(like);
            return true;
        }


    }
}
