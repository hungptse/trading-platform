package com.aquariux.trading_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBalance {
    private Long userId;
    private Map<String, Double> balance; // <symbol, balance>
}
