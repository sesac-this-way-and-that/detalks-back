package com.twat.detalks.member.exception;

import com.twat.detalks.member.dto.ResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

@RestControllerAdvice
// 전역 예외처리를 위한 클래스
public class GlobalExceptionHandler {

    // DTO 유효성 검사 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> bindExceptionHandler(final MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        return ResponseEntity.badRequest().body(
            ResDto.builder()
                .result(false)
                .status("400")
                .msg(Objects.requireNonNull(fieldError).getDefaultMessage())
                .errorType(e.getClass().toString())
                .build());
    }

    // 서비스 예외처리 핸들러
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentExceptionHandler(final IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(
            ResDto.builder()
                .result(false)
                .status("400")
                .msg(e.getMessage())
                .errorType(e.getClass().toString())
                .build());
    }

    // 엔드포인트 예외처리 핸들러
    @ExceptionHandler({NoResourceFoundException.class, NumberFormatException.class})
    public ResponseEntity<?> endPointExceptionHandler(Exception e) {
        String errorMessage;
        if (e instanceof NoResourceFoundException) {
            errorMessage = "페이지를 찾을 수 없습니다.";
        } else if (e instanceof NumberFormatException) {
            errorMessage = "잘못된 형식의 요청 입니다.";
        } else {
            errorMessage = "알 수 없는 오류가 발생했습니다.";
        }

        return ResponseEntity.badRequest().body(
            ResDto.builder()
                .result(false)
                .status("400")
                .msg(errorMessage)
                .errorType(e.getClass().toString())
                .build());
    }


}
