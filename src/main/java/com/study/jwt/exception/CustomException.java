package com.study.jwt.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
