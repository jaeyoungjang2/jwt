package com.study.jwt.post.service;

import com.study.jwt.account.entity.Account;
import com.study.jwt.like.entity.Like;
import com.study.jwt.like.repository.LikeRepository;
import com.study.jwt.post.dto.PostRequestDto;
import com.study.jwt.post.dto.PostResponseDto;
import com.study.jwt.post.entity.Post;
import com.study.jwt.post.repository.PostRepository;
import com.study.jwt.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public PostResponseDto savePost(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost);
    }

    public PostResponseDto findPost(UserDetailsImpl userDetails, Long id) {
        Account account = userDetails.getAccount();

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물을 찾을 수 없습니다.")
        );

        // 게시글의 좋아요 수 확인
        List<Like> likes = post.getLikes();
        post.updateLikeCount(likes.size());

        // 좋아요 클릭 여부
        if (likeRepository.existsByPostAndAccount(post, account)) {
            post.updateLikeState(true);
        }

        return new PostResponseDto(post);
    }

    public List<PostResponseDto> findAllPost() {
        List<Post> foundPosts = postRepository.findAll();

        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post foundPost : foundPosts) {
            postResponseDtos.add(new PostResponseDto(foundPost));
        }

        return postResponseDtos;
    }
}
