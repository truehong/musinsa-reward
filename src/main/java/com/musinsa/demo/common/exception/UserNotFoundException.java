package com.musinsa.demo.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class UserNotFoundException extends ServiceNotFoundException {
    public UserNotFoundException(String userId) {
        super(ServiceNotFoundErrorType.USER);
        log.info("not found user id - {}", userId);
    }
}
