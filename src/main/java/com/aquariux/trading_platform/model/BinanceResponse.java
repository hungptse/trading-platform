package com.aquariux.trading_platform.model;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class BinanceResponse {
    private String symbol;
    private double bidPrice;
    private double askPrice;
}
