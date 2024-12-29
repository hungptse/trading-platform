package com.aquariux.trading_platform.controller;


import com.aquariux.trading_platform.entity.AggregatedPrice;
import com.aquariux.trading_platform.model.TradeCommand;
import com.aquariux.trading_platform.model.UserBalance;
import com.aquariux.trading_platform.service.PriceAggregationService;
import com.aquariux.trading_platform.service.TradingService;
import com.aquariux.trading_platform.service.UserWalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/trade")
public class TradeController {


    private final TradingService tradingService;

    public TradeController(TradingService tradingService) {
        this.tradingService = tradingService;
    }


    @PostMapping("/send")
    public ResponseEntity<String> executeTrade(@RequestBody TradeCommand body) {
        return ResponseEntity.ok(tradingService.executeTrade(body.getUserId(), body.getTradingPair(), body.getOrderType(), body.getQuantity()));
    }
}
