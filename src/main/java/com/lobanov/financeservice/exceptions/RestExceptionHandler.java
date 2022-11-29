package com.lobanov.financeservice.exceptions;

import com.lobanov.financeservice.dtos.responses.CustomResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static java.time.LocalDateTime.now;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("exception", ex.getClass().getName());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CustomResponseDto.builder()
                        .timeStamp(now())
                        .message(ex.getMessage())
                        .errors(errorMap)
                        .build()
                );
    }

    @ExceptionHandler(value = NotEnoughMoneyException.class)
    public ResponseEntity<Object> handleNotEnoughMoneyException(NotEnoughMoneyException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("exception", ex.getClass().getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomResponseDto.builder()
                        .timeStamp(now())
                        .message(ex.getMessage())
                        .errors(errorMap)
                        .build()
                );
    }

    @ExceptionHandler(value = WrongSecretKeyException.class)
    public ResponseEntity<Object> handleWrongSecretKeyException(WrongSecretKeyException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("exception", ex.getClass().getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomResponseDto.builder()
                        .timeStamp(now())
                        .message(ex.getMessage())
                        .errors(errorMap)
                        .build()
                );
    }

    @ExceptionHandler(value = ValidityExpiredException.class)
    public ResponseEntity<Object> handleValidityExpiredException(ValidityExpiredException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("exception", ex.getClass().getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomResponseDto.builder()
                        .timeStamp(now())
                        .message(ex.getMessage())
                        .errors(errorMap)
                        .build()
                );
    }

}
