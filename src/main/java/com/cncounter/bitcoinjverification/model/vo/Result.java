package com.cncounter.bitcoinjverification.model.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;


    public static <T> Result<T> failure(String msg) {
        return of(200, msg, null);
    }

    public static <T> Result<T> success(T data) {
        return of(200, "success", data);
    }

    public static <T> Result<T> of(int code, String message, T data) {
        Result<T> result = (Result<T>) Result.builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
        return result;
    }

    public static Result of(int code) {
        return Result.builder()
                .code(code)
                .build();
    }

}
