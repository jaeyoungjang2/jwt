package com.study.jwt.post.dto;

import com.study.jwt.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {

    private String title;
    private String contents;

    private int likeCount;

    private boolean likeState;
    public PostResponseDto(Post savedPost) {
        this.title = savedPost.getTitle();
        this.contents = savedPost.getContents();
        this.likeState = savedPost.isLikeState();
        this.likeCount = savedPost.getLikeCount();
    }
}
