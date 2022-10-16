package com.study.jwt.post.service;

import com.study.jwt.post.dto.PostRequestDto;
import com.study.jwt.post.dto.PostResponseDto;
import com.study.jwt.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto savePost(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost);
    }

    public PostResponseDto findPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물을 찾을 수 없습니다.")
        );

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
