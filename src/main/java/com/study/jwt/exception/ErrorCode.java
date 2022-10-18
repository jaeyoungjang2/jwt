package com.study.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    NotFoundPost(HttpStatus.NOT_FOUND.value(), "P001", "게시물을 찾을 수 없습니다.")
    ;

    private final int httpStatus;
    private final String errorCode;
    private final String message;

    }
