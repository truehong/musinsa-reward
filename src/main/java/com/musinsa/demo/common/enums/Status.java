package com.musinsa.demo.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    OPEN(0), CLOSED(1), PAUSE(2), RESUME(3);
    private final int value;
    public int getValue() {
        return value;
    }
};