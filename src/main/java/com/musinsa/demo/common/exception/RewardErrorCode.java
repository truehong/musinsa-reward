package com.musinsa.demo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RewardErrorCode {
    USER_DUPLICATE_REGISTER(10001, "user already received reward"),
    OUT_OF_REWARD_STOCK(10002, "rewards is out of stock"),
    NOT_FOUND_REWARD(10002, "not found reward"),
    NOT_FOUND_USER(10002, "not found user");

    private final Integer code;
    private final String message;
}
