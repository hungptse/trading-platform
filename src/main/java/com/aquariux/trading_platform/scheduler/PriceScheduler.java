package com.aquariux.trading_platform.scheduler;

import com.aquariux.trading_platform.service.PriceAggregationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PriceScheduler {

    @Autowired
    private PriceAggregationService priceService;

    @Scheduled(fixedDelay = 10000)
    public void updatePrices() {
        priceService.updatePrices();
    }
}