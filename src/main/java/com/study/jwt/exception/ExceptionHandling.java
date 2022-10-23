package com.study.jwt.exception;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling {

    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandle(CustomException e) {

        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }


    @RequiredArgsConstructor
    private static class ErrorResponse {
        private final int httpStatus;
        private final String errorCode;
        private final String message;

        public static ResponseEntity toResponseEntity(ErrorCode errorCode) {
            return ResponseEntity
                    .status(errorCode.getHttpStatus())
                    .body(new ErrorResponse(errorCode.getHttpStatus(), errorCode.getErrorCode(), errorCode.getMessage()));

        }
    }
}


