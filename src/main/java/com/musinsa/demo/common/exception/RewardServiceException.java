package com.musinsa.demo.common.exception;

import lombok.Getter;

@Getter
public class RewardServiceException extends RuntimeException{
    private final RewardErrorCode errorCode;
    private final String message;

    public RewardServiceException(RewardErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
