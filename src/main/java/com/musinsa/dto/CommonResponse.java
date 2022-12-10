package com.musinsa.dto;

import org.springframework.http.HttpStatus;

import javax.net.ssl.SSLEngineResult;

public class CommonResponse <T>{
    private Integer code;
    private String message;
    private T value;

    protected CommonResponse(T value) {
        this.value = value;
    }

    protected CommonResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CommonResponse<Void> errorOf(Integer code, String message) {
        return new CommonResponse(code, message);
    }
}
