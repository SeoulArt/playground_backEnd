package com.skybory.seoulArt.global.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", errorCode.getStatus());
        body.put("error", HttpStatus.valueOf(errorCode.getStatus()).getReasonPhrase());
        body.put("code", errorCode.getCode());
        body.put("message", errorCode.getMessage());

        return new ResponseEntity<>(body, HttpStatus.valueOf(errorCode.getStatus()));
    }
}