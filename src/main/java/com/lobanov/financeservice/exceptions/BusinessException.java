package com.lobanov.financeservice.exceptions;

public class BusinessException extends RuntimeException {

    public BusinessException() {
        super("BusinessException error");
    }

    public BusinessException(String msg) {
        super(msg);
    }
}
