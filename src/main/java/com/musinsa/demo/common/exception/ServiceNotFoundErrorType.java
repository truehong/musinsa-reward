package com.musinsa.demo.common.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ServiceNotFoundErrorType implements ErrorType {
    USER("user not found"),
    REWARD("reward not found");
    private final HttpStatus statusCode = HttpStatus.NOT_FOUND;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
