package com.aquariux.trading_platform.model;

import com.aquariux.trading_platform.expection.TFErrorCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TFResponse<T> {
    @Builder.Default
    private Integer code = TFErrorCode.SUCCESS.getCode();
    @Builder.Default
    private String errorMsg = TFErrorCode.SUCCESS.getMessage();
    private T data;
}
