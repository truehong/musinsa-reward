package com.musinsa.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RewardErrorCode {
    USER_DUPLICATE_REGISTER(10001,"user already receive reward");

    private final Integer code;
    private final String message;
}
