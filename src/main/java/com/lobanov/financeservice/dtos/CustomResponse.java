package com.lobanov.financeservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CustomResponse {
    private Integer statusCode;
    private LocalDateTime timeStamp;
    private HttpStatus status;
    private String response;
    private String message;
    private String developerMessage;
    private Map<String,?> data;
    private Map<String, ?> errors;
}