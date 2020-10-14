package com.trade.calculation.exception;

public enum Errors {
    INVALID_TRADE_EVENT("TCS-001", "Invalid Trade Event");

    private final String errorCode;
    private final String errorMessage;

    Errors(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
