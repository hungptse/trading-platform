package com.aquariux.trading_platform.expection;

import com.aquariux.trading_platform.model.TFResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ TFException.class, RuntimeException.class})
    public ResponseEntity<TFResponse> handleException(TFException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TFResponse.builder().code(e.getCode()).errorMsg(e.getErrorMsg()).build());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public TFResponse handleUnwantedException(Exception e) {
        e.printStackTrace();
        return TFResponse.builder().code(TFErrorCode.INTERNAL_SERVER_ERROR.getCode()).errorMsg(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public TFResponse handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return TFResponse.builder().code(TFErrorCode.INTERNAL_SERVER_ERROR.getCode()).errorMsg("Validation failed").data(errors).build();
    }
}
