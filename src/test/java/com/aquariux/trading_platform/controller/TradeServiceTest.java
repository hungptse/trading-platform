package com.aquariux.trading_platform.controller;

import com.aquariux.trading_platform.entity.AggregatedPrice;
import com.aquariux.trading_platform.entity.Transaction;
import com.aquariux.trading_platform.model.UserBalance;
import com.aquariux.trading_platform.service.PriceAggregationService;
import com.aquariux.trading_platform.service.TradingService;
import com.aquariux.trading_platform.service.TransactionService;
import com.aquariux.trading_platform.service.UserWalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {

    @Mock
    private PriceAggregationService priceAggregationService;

    @Mock
    private UserWalletService userWalletService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TradingService tradeService;

    @Test
    void testExecuteTrade_BuyOrder_Success() {
        Long userId = 1L;
        String tradingPair = "BTCUSDT";
        String orderType = "BUY";
        Double quantity = 0.1;

        // Mock aggregated price
        AggregatedPrice price = new AggregatedPrice();
        price.setTradingPair(tradingPair);
        price.setAskPrice(30000.0);
        price.setBidPrice(29900.0);
        when(priceAggregationService.findByTradingPair(tradingPair)).thenReturn(price);

        // Mock user wallet balance
        Map<String, Double> balance = new HashMap<>();
        balance.put("USDT", 50000.0);
        balance.put("BTC", 0.0);
        when(userWalletService.getBalance(userId)).thenReturn(new UserBalance(userId, balance));

        // Simulate updated balance after trade
        Map<String, Double> updatedBalance = new HashMap<>();
        updatedBalance.put("USDT", 47000.0); // Spent 3000 USDT (price: 30000 * quantity: 0.1)
        updatedBalance.put("BTC", 0.1); // Bought 0.1 BTC
        doNothing().when(userWalletService).updateBalance(userId, updatedBalance);

        // Simulate transaction insertion
        doNothing().when(transactionService).insertTransaction(any(Transaction.class));

        // Execute the trade
        String result = tradeService.executeTrade(userId, tradingPair, orderType, quantity);

        // Verify results
        assertEquals("Trade executed successfully", result);
        verify(priceAggregationService).findByTradingPair(tradingPair);
        verify(userWalletService).getBalance(userId);
        verify(userWalletService).updateBalance(userId, updatedBalance);
        verify(transactionService).insertTransaction(any(Transaction.class));
    }

//    @Test
//    void testExecuteTrade_InvalidTradingPair() {
//        Long userId = 1L;
//        String tradingPair = "INVALID";
//        String orderType = "BUY";
//        Double quantity = 0.1;
//
//        // Mock aggregated price not found
//        when(priceAggregationService.findByTradingPair(tradingPair)).thenReturn(null);
//
//        // Execute the trade
//        String result = tradeService.executeTrade(userId, tradingPair, orderType, quantity);
//
//        // Verify results
//        assertEquals("Invalid trading pair or user ID", result);
//        verify(priceAggregationService).findByTradingPair(tradingPair);
//        verifyNoInteractions(userWalletService);
//        verifyNoInteractions(transactionService);
//    }
//
//    @Test
//    void testExecuteTrade_InsufficientBalance() {
//        Long userId = 1L;
//        String tradingPair = "BTCUSDT";
//        String orderType = "BUY";
//        Double quantity = 2.0;
//
//        // Mock aggregated price
//        AggregatedPrice price = new AggregatedPrice();
//        price.setTradingPair(tradingPair);
//        price.setAskPrice(30000.0);
//        price.setBidPrice(29900.0);
//        when(priceAggregationService.findByTradingPair(tradingPair)).thenReturn(price);
//
//        // Mock user wallet balance
//        Map<String, Double> balance = new HashMap<>();
//        balance.put("USDT", 1000.0); // Not enough to buy
//        balance.put("BTC", 0.0);
//        when(userWalletService.getBalance(userId)).thenReturn(new WalletResponse(balance));
//
//        // Execute the trade
//        String result = tradeService.executeTrade(userId, tradingPair, orderType, quantity);
//
//        // Verify results
//        assertEquals("Invalid trading pair or user ID", result); // Adjust if custom error is implemented for insufficient funds
//        verify(priceAggregationService).findByTradingPair(tradingPair);
//        verify(userWalletService).getBalance(userId);
//        verifyNoInteractions(transactionService);
//    }
}
