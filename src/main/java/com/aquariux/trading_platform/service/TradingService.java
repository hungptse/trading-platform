package com.aquariux.trading_platform.service;

import com.aquariux.trading_platform.entity.AggregatedPrice;
import com.aquariux.trading_platform.entity.Transaction;
import com.aquariux.trading_platform.entity.UserWallet;
import com.aquariux.trading_platform.expection.TFErrorCode;
import com.aquariux.trading_platform.expection.TFException;
import com.aquariux.trading_platform.model.TFResponse;
import com.aquariux.trading_platform.model.UserBalance;
import com.aquariux.trading_platform.repository.AggregatedPriceRepository;
import com.aquariux.trading_platform.repository.UserWalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TradingService {

    private final UserWalletService userWalletService;
    private final PriceAggregationService priceAggregationService;
    private final TransactionService transactionService;

    public TradingService(UserWalletService userWalletService, PriceAggregationService priceAggregationService, TransactionService transactionService) {
        this.userWalletService = userWalletService;
        this.priceAggregationService = priceAggregationService;
        this.transactionService = transactionService;
    }

    @Transactional
    public void executeTrade(Long userId, String tradingPair, String orderType, Double quantity) throws TFException {
        var response = TFResponse.builder();
        AggregatedPrice price = priceAggregationService.findByTradingPair(tradingPair);
        var userWallet = userWalletService.getBalance(userId);

        if (price == null || userWallet == null) {
            throw new TFException(TFErrorCode.INVALID_TRADING_PAIR_OR_USER_ID);
        }
        var balance = userWallet.getBalance();

        double tradePrice = orderType.equals("BUY") ? price.getAskPrice() : price.getBidPrice();
        var newBalance = balance;
        switch (tradingPair) {
            case "BTCUSDT":
                if (orderType.equals("BUY")) {
                    newBalance = buy("USDT", "BTC", balance, quantity, price.getAskPrice());
                } else if (orderType.equals("SELL")){
                    newBalance = sell("BTC", "USDT", balance, quantity, price.getBidPrice());
                }
                break;
            case "ETHUSDT":
                if (orderType.equals("BUY")) {
                    newBalance = buy("USDT", "ETH", balance, quantity, price.getAskPrice());
                } else if (orderType.equals("SELL")){
                    newBalance = sell("ETH", "USDT", balance, quantity, price.getBidPrice());
                }
                break;
            default:
                throw new TFException(TFErrorCode.INVALID_TRADING_PAIR_OR_USER_ID);
        }

        userWalletService.updateBalance(userId, newBalance);

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setTradingPair(tradingPair);
        transaction.setOrderType(orderType);
        transaction.setPrice(tradePrice);
        transaction.setQuantity(quantity);
        transaction.setTimestamp(LocalDateTime.now());
        transactionService.insertTransaction(transaction);
    }

    private Map<String, Double> buy(String source, String dest, Map<String, Double> balance, double quantity, double tradePrice) throws TFException {
        double totalCost = tradePrice * quantity;
        var balanceSource = balance.get(source);

        if(balanceSource < totalCost) {
            throw new TFException(TFErrorCode.INSUFFICIENT_BALANCE);
        }

        balance.replace(source, balanceSource - totalCost);
        balance.replace(dest, balance.getOrDefault(dest, 0.0) + quantity);
        return balance;
    }

    private Map<String, Double> sell(String source, String dest, Map<String, Double> balance, double quantity,
                      double tradePrice) throws TFException {
        double totalCost = tradePrice * quantity;
        var balanceSource = balance.get(source);

        if (quantity > balanceSource) {
            throw new TFException(TFErrorCode.INSUFFICIENT_BALANCE);
        }
        balance.replace(source, balanceSource - quantity);
        balance.replace(dest, balance.getOrDefault(dest, 0.0) + totalCost);
        return balance;
    }
}
