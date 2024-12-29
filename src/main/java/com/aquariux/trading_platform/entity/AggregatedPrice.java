package com.aquariux.trading_platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregatedPrice {
    @Id
    private String tradingPair;
    private Double bidPrice;
    private Double askPrice;
    private LocalDateTime updatedAt;

}
