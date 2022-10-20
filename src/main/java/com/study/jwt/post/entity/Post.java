package com.study.jwt.post.entity;

import com.study.jwt.like.entity.Like;
import com.study.jwt.post.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String contents;

    @OneToMany(mappedBy = "post")
    private List<Like> likes;

    private int likeCount;

    @Transient
    private boolean likeState;

    public Post(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
    }

    public void updateLikeCount(int size) {
        this.likeCount = size;
    }

    public void updateLikeState(boolean state) {
        this.likeState = state;
    }
}
