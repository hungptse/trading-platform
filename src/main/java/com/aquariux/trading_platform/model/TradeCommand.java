package com.aquariux.trading_platform.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeCommand {

    @NotBlank(message = "tradingPair is mandatory")
    private String tradingPair;

    @Pattern(regexp = "BUY|SELL", message = "orderType must be either 'BUY' or 'SELL'")
    @NotBlank(message = "orderType is mandatory")
    private String orderType;

    @NotNull(message = "quantity is mandatory")
    @Positive
    private Double quantity;

    @NotNull(message = "userId is mandatory")
    @Positive
    private Long userId;

}
