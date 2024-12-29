package com.aquariux.trading_platform.expection;

import com.aquariux.trading_platform.model.TFResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ TFException.class, RuntimeException.class})
    public ResponseEntity<TFResponse> handleException(TFException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TFResponse.builder().code(e.getCode()).errorMsg(e.getErrorMsg()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Unknow error");
    }
}
