package com.musinsa.demo.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorType {
    HttpStatus getHttpStatus();
    String getMessage();
}
