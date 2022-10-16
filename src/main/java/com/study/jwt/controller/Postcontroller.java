package com.study.jwt.controller;

import com.study.jwt.post.dto.PostRequestDto;
import com.study.jwt.post.dto.PostResponseDto;
import com.study.jwt.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class Postcontroller {

    private final PostService postService;

    @PostMapping("")
    private PostResponseDto savePost(PostRequestDto postRequestDto) {
        return postService.savePost(postRequestDto);
    }
}
