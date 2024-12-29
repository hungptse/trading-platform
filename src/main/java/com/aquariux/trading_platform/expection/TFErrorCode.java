package com.aquariux.trading_platform.expection;

public enum TFErrorCode {
    INVALID_TRADING_PAIR_OR_USER_ID(-1001, "Invalid trading pair or user ID"),
    INSUFFICIENT_BALANCE(-1002, "Insufficient balance"),
    PRICE_NOT_FOUND(-1003, "Price not available for the trading pair"),
    USER_NOT_FOUND(-1004, "User not existed"),
    SUCCESS(0, "Success"),
    UNKNOWN_ERROR(-999, "An unknown error occurred"),
    INTERNAL_SERVER_ERROR(-998, "An unknown error occurred");

    private final int code;
    private final String message;

    TFErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
