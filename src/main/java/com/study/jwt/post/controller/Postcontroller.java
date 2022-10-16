package com.study.jwt.post.controller;

import com.study.jwt.post.dto.PostRequestDto;
import com.study.jwt.post.dto.PostResponseDto;
import com.study.jwt.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class Postcontroller {

    private final PostService postService;

    // 게시글 저장
    @PostMapping("")
    private PostResponseDto savePost(PostRequestDto postRequestDto) {
        return postService.savePost(postRequestDto);
    }

    // 게시글 단건 조회
    @GetMapping("/{id}")
    private PostResponseDto findPost(@PathVariable Long id) {
        return postService.findPost(id);
    }

    // 게시글 전체 조회
    @GetMapping("")
    private List<PostResponseDto> findAllPost() {
        return postService.findAllPost();
    }
}
