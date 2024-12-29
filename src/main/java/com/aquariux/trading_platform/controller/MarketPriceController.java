package com.aquariux.trading_platform.controller;

import com.aquariux.trading_platform.entity.AggregatedPrice;
import com.aquariux.trading_platform.model.TFResponse;
import com.aquariux.trading_platform.service.PriceAggregationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/price")
public class MarketPriceController {


    private final PriceAggregationService priceAggregationService;

    public MarketPriceController(PriceAggregationService priceAggregationService) {
        this.priceAggregationService = priceAggregationService;
    }

    @GetMapping("/latest")
    public TFResponse<List<AggregatedPrice>> getLatestPrice() {
        return TFResponse.<List<AggregatedPrice>>builder().data(priceAggregationService.getLatestPrice()).build();
    }
}
