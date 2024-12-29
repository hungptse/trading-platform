package com.aquariux.trading_platform.controller;

import com.aquariux.trading_platform.entity.AggregatedPrice;
import com.aquariux.trading_platform.entity.Transaction;
import com.aquariux.trading_platform.expection.TFErrorCode;
import com.aquariux.trading_platform.expection.TFException;
import com.aquariux.trading_platform.model.UserBalance;
import com.aquariux.trading_platform.service.PriceAggregationService;
import com.aquariux.trading_platform.service.TransactionService;
import com.aquariux.trading_platform.service.TradingService;
import com.aquariux.trading_platform.service.UserWalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradingServiceTest {

    @Mock
    private UserWalletService userWalletService;

    @Mock
    private PriceAggregationService priceAggregationService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TradingService tradingService;

    @Test
    void executeTrade_BuyOrder_Success() throws TFException {
        // Arrange
        Long userId = 1000L;
        String tradingPair = "BTCUSDT";
        String orderType = "BUY";
        Double quantity = 0.1;

        AggregatedPrice price = new AggregatedPrice();
        price.setTradingPair(tradingPair);
        price.setAskPrice(30000.0);
        price.setBidPrice(29900.0);

        Map<String, Double> balance = new HashMap<>();
        balance.put("USDT", 50000.0);
        balance.put("BTC", 0.0);

        UserBalance userBalance = new UserBalance();
        userBalance.setBalance(balance);

        when(priceAggregationService.findByTradingPair(tradingPair)).thenReturn(price);
        when(userWalletService.getBalance(userId)).thenReturn(userBalance);
        doNothing().when(userWalletService).updateBalance(eq(userId), anyMap());
        doNothing().when(transactionService).insertTransaction(any(Transaction.class));

        // Act
        tradingService.executeTrade(userId, tradingPair, orderType, quantity);

        // Assert
        verify(priceAggregationService, times(1)).findByTradingPair(tradingPair);
        verify(userWalletService, times(1)).getBalance(userId);
        verify(userWalletService, times(1)).updateBalance(eq(userId), anyMap());
        verify(transactionService, times(1)).insertTransaction(any(Transaction.class));
    }

    @Test
    void executeTrade_InvalidTradingPair_ThrowsException() {
        // Arrange
        Long userId = 1000L;
        String tradingPair = "INVALID";
        String orderType = "BUY";
        Double quantity = 0.1;

        when(priceAggregationService.findByTradingPair(tradingPair)).thenReturn(null);

        // Act & Assert
        TFException exception = assertThrows(
                TFException.class,
                () -> tradingService.executeTrade(userId, tradingPair, orderType, quantity)
        );

        assertEquals(TFErrorCode.INVALID_TRADING_PAIR_OR_USER_ID.getCode(), exception.getCode());
        verify(priceAggregationService, times(1)).findByTradingPair(tradingPair);
    }

    @Test
    void executeTrade_InsufficientBalance_ThrowsException() throws TFException {
        // Arrange
        Long userId = 1000L;
        String tradingPair = "BTCUSDT";
        String orderType = "BUY";
        Double quantity = 2.0;

        AggregatedPrice price = new AggregatedPrice();
        price.setTradingPair(tradingPair);
        price.setAskPrice(30000.0);
        price.setBidPrice(29900.0);

        Map<String, Double> balance = new HashMap<>();
        balance.put("USDT", 1000.0); // Insufficient balance
        balance.put("BTC", 0.0);

        UserBalance userBalance = new UserBalance();
        userBalance.setBalance(balance);

        when(priceAggregationService.findByTradingPair(tradingPair)).thenReturn(price);
        when(userWalletService.getBalance(userId)).thenReturn(userBalance);

        // Act & Assert
        TFException exception = assertThrows(
                TFException.class,
                () -> tradingService.executeTrade(userId, tradingPair, orderType, quantity)
        );

        assertEquals(TFErrorCode.INSUFFICIENT_BALANCE.getCode(), exception.getCode());
        verify(priceAggregationService, times(1)).findByTradingPair(tradingPair);
        verify(userWalletService, times(1)).getBalance(userId);
        verifyNoInteractions(transactionService);
    }
}
