package com.lobanov.financeservice.exceptions;

public class WrongSecretKeyException extends BusinessException {
    public WrongSecretKeyException(String msg) {
        super(msg);
    }
}
