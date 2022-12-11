package com.musinsa.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CommonResponse<T> {
    private HttpStatus status;
    private String message;
    private T value;

    public CommonResponse(T value) {
        this.status = HttpStatus.OK;
        this.value = value;
    }

    protected CommonResponse(HttpStatus status) {
        this.status = status;
    }

    protected CommonResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public static CommonResponse<Void> errorOf(HttpStatus status, String message) {
        return new CommonResponse(status, message);
    }

    public static CommonResponse<Void> ok() {
        return new CommonResponse<>(HttpStatus.OK);
    }
}
