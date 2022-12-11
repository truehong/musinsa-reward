package com.musinsa.demo.common.exception;

import lombok.Getter;

@Getter
public class ServiceNotFoundException extends RuntimeException {
    private final ServiceNotFoundErrorType errorType;
    public ServiceNotFoundException(ServiceNotFoundErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
