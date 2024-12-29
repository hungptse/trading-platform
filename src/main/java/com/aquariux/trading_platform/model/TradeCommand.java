package com.aquariux.trading_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeCommand {
    private String tradingPair;
    private String orderType;
    private Double quantity;
    private Long userId;

}
