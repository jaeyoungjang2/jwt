package com.study.jwt.like.controller;

import com.study.jwt.account.entity.Account;
import com.study.jwt.like.dto.LikeRequestDto;
import com.study.jwt.like.service.LikeService;
import com.study.jwt.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like")
    public boolean addLikeOrDeleteLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikeRequestDto likeRequestDto) {
        Account account = userDetails.getAccount();
        return likeService.addLikeOrDeleteLike(account, likeRequestDto);
    }
}
