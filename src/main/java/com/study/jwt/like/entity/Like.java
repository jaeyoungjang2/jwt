package com.study.jwt.like.entity;

import com.study.jwt.account.entity.Account;
import com.study.jwt.post.entity.Post;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Account account;

    public Like(Account account, Post post) {
        this.post = post;
        this.account = account;
    }
}
