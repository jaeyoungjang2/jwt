package com.study.jwt.post.dto;

import com.study.jwt.post.service.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {

    private String title;
    private String contents;
    public PostResponseDto(Post savedPost) {

    }
}
