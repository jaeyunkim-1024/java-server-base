package com.sample.base.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorExceptionHandler {
    @ExceptionHandler(value = {RuntimeException.class, MethodArgumentNotValidException.class, AuthenticationServiceException.class})
    public ResponseEntity<String> handleIllegalArgumentException(Exception e){
        return ResponseEntity
                .status(401)
                .body(e.getMessage());
    }
}
