package com.study.jwt.post.service;

import com.study.jwt.post.dto.PostRequestDto;
import com.study.jwt.post.dto.PostResponseDto;
import com.study.jwt.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto savePost(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost);
    }
}
