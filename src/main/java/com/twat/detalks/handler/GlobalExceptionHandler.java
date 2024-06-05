package com.twat.detalks.handler;

import com.twat.detalks.dto.ResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유효성 검사 예외처리 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> bindExceptionHandler(final MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        return ResponseEntity.badRequest().body(
            ResDto.builder()
                .result(false)
                .msg(Objects.requireNonNull(fieldError).getDefaultMessage())
                .errorType(e.getClass().toString())
                .build());
    }

    // 파라미터 예외처리 핸들러
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentExceptionHandler(final IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(
            ResDto.builder()
                .result(false)
                .msg(e.getMessage())
                .errorType(e.getClass().toString())
                .build());
    }
}
