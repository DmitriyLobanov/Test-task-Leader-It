package com.lobanov.financeservice.exceptions;

public class NotEnoughMoneyException extends BusinessException {
    public NotEnoughMoneyException(String msg) {
        super(msg);
    }
}
