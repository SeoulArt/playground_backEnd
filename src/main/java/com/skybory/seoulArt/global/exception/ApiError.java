package com.skybory.seoulArt.global.exception;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String code;
    private String message;

    // 생성자, getter 및 setter
    public ApiError(LocalDateTime timestamp, int status, String code, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.code = code;
        this.message = message;
    }
}