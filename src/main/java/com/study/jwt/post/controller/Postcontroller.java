package com.study.jwt.post.controller;

import com.study.jwt.post.dto.PostRequestDto;
import com.study.jwt.post.dto.PostResponseDto;
import com.study.jwt.post.service.PostService;
import com.study.jwt.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Postcontroller {

    private final PostService postService;

    // 게시글 저장
    @PostMapping("/api/posts")
    private PostResponseDto savePost(@RequestBody PostRequestDto postRequestDto) {
        return postService.savePost(postRequestDto);
    }

    // 게시글 단건 조회
    @GetMapping("/api/posts/{id}")
    private PostResponseDto findPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        return postService.findPost(userDetails, id);
    }

    // 게시글 전체 조회
    @GetMapping("/api/posts")
    private List<PostResponseDto> findAllPost() {
        return postService.findAllPost();
    }
}
