package com.musinsa.demo.common.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ServiceErrorType implements ErrorType{
    USER_DUPLICATE_REGISTER("user already received reward"),
    OUT_OF_REWARD_STOCK("rewards is out of stock"),
    BAD_REQUEST_VALIDATION("validation failed for argument"),
    UNKNOWN_SERVER_ERROR("unknown server error");


    private final HttpStatus statusCode = HttpStatus.BAD_REQUEST;
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
