package com.aquariux.trading_platform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String tradingPair; // BTCUSDT or ETHUSDT
    private String orderType; // BUY or SELL
    private Double price;
    private Double quantity;
    private LocalDateTime timestamp;
}
