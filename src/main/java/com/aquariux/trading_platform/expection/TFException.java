package com.aquariux.trading_platform.expection;

import lombok.Data;

@Data
public class TFException extends Exception {
    private int code;
    private String errorMsg;

    public TFException(TFErrorCode ex) {
        this.code = ex.getCode();
        this.errorMsg = ex.getMessage();
    }
}
