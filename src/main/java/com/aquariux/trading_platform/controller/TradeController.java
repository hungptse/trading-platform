package com.aquariux.trading_platform.controller;


import com.aquariux.trading_platform.entity.AggregatedPrice;
import com.aquariux.trading_platform.expection.TFException;
import com.aquariux.trading_platform.model.TFResponse;
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


    @PostMapping("/order")
    public TFResponse executeTrade(@RequestBody TradeCommand body) throws TFException {
        tradingService.executeTrade(body.getUserId(), body.getTradingPair(), body.getOrderType(),
                body.getQuantity());
        return TFResponse.builder().build();
    }
}
