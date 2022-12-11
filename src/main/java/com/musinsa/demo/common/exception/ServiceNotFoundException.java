package com.musinsa.demo.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ServiceNotFoundException extends RuntimeException {
    private final ServiceNotFoundErrorType errorType;
    public ServiceNotFoundException(ServiceNotFoundErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
