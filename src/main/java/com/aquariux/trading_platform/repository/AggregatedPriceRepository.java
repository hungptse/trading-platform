package com.aquariux.trading_platform.repository;

import com.aquariux.trading_platform.entity.AggregatedPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AggregatedPriceRepository extends JpaRepository<AggregatedPrice, Long> {
    AggregatedPrice findByTradingPair(String tradingPair);
}
