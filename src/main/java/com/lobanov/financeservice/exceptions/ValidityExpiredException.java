package com.lobanov.financeservice.exceptions;

public class ValidityExpiredException extends BusinessException {
    public ValidityExpiredException(String msg) {
        super(msg);
    }
}
