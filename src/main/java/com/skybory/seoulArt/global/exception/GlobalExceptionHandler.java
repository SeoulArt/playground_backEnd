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
    public ResponseEntity<ApiError> handleServiceException(ServiceException ex) {
        ErrorCode code = ex.getErrorCode();
        ApiError error = new ApiError(LocalDateTime.now(), code.getStatus(), code.getCode(), code.getMessage());
        return new ResponseEntity<>(error, HttpStatus.valueOf(code.getStatus()));
    }
}
