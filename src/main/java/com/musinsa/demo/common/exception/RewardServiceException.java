package com.musinsa.demo.common.exception;

import lombok.Getter;

@Getter
public class RewardServiceException extends RuntimeException {
    private final ServiceErrorType errorType;

    public RewardServiceException(ServiceErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
