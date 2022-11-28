package com.lobanov.financeservice.enums;

public enum ExecutionResult {
    SUCCESS,
    FAILED_WRONG_SECRET_KEY,
    FAILED_NOT_ENOUGH_MONEY,

    FAILED_VALIDITY_EXPIRED
}
