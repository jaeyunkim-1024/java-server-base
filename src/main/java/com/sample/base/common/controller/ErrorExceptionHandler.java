package com.sample.base.common.controller;

import com.sample.base.common.dto.CustomResponseDto;
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
    public ResponseEntity<CustomResponseDto<String>> handleIllegalArgumentException(Exception e){
        return ResponseEntity.ok(
                CustomResponseDto.<String>builder()
                    .data(e.getMessage())
                    .build()
                );
    }
}
