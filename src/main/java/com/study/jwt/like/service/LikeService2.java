package com.study.jwt.like.service;

import com.study.jwt.account.entity.Account;
import com.study.jwt.like.dto.LikeRequestDto;
import com.study.jwt.like.entity.Like;
import com.study.jwt.like.repository.LikeRepository;
import com.study.jwt.post.entity.Post;
import com.study.jwt.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService2
{
    private final PostRepository postRepository;

    private final LikeRepository likeRepository;

    @Transactional
    public boolean addLikeOrDeleteLike(Account account, LikeRequestDto likeRequestDto) {

        Post post = postRepository.findById(likeRequestDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );

        // 좋아요를 누른 상태인지, 누르지 않은 상태인지 확인
        Optional<Like> foundLike = likeRepository.findByPostAndAccount(post, account);

        if (foundLike.isPresent()) {
            System.out.println("--------------------------1-----------------------------------------");
            likeRepository.delete(foundLike.get());
            System.out.println("--------------------------2-----------------------------------------");

            // 게시글의 좋아요 수 변경
            System.out.println("--------------------------3-----------------------------------------");
            List<Like> likes = post.getLikes();
            System.out.println("--------------------------4-----------------------------------------");
            post.updateLikeCount(likes.size());
            System.out.println("-------------------------------------------------------------------");
            System.out.println("likes.size() = " + likes.size());
//            post.updateLikeCount(likeRepository.findAllByPostId(likeRequestDto.getPostId()).size());

            return false;
        } else {
            Like like = new Like(account, post);
            System.out.println("--------------------------1-----------------------------------------");
            likeRepository.save(like);

            // 게시글의 좋아요 수 변경
            System.out.println("--------------------------2-----------------------------------------");
            List<Like> likes = post.getLikes();
            System.out.println("--------------------------3-----------------------------------------");
            post.updateLikeCount(likes.size());
            System.out.println("--------------------------4-----------------------------------------");
            System.out.println("likes.size() = " + likes.size());
            return true;
        }
    }
}
