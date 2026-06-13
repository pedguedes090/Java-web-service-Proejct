package com.example.project.controller;

import com.example.project.models.dto.res.responseDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<responseDto<Object>> handleNotFound(EntityNotFoundException ex) {
        log.warn("Loi khong tim thay: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseDto.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .errors(null)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<responseDto<Object>> handleBadRequest(IllegalArgumentException ex) {
        log.warn("Loi request khong hop le: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(responseDto.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .errors(null)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<responseDto<Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .orElse("Du lieu khong hop le");
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(java.util.stream.Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage() == null ? "Khong hop le" : error.getDefaultMessage(),
                        (oldValue, newValue) -> oldValue
                ));
        log.warn("Loi validate request: {}", errors);
        return ResponseEntity.badRequest()
                .body(responseDto.builder()
                        .success(false)
                        .message(message)
                        .data(null)
                        .errors(errors)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build());
    }
}
