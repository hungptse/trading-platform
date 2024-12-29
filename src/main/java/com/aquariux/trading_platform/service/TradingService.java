package com.aquariux.trading_platform.service;

import com.aquariux.trading_platform.entity.AggregatedPrice;
import com.aquariux.trading_platform.entity.Transaction;
import com.aquariux.trading_platform.entity.UserWallet;
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
    public String executeTrade(Long userId,String tradingPair, String orderType, Double quantity) {
        AggregatedPrice price = priceAggregationService.findByTradingPair(tradingPair);
        Map<String, Double> balance = userWalletService.getBalance(userId).getBalance(); // <symbol, balance>

        if (price == null || balance.isEmpty()) {
            return "Invalid trading pair or user ID";
        }

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
                throw new RuntimeException("Invalid trading pair");
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
        return "Trade executed successfully";
    }

    private Map<String, Double> buy(String source, String dest, Map<String, Double> balance, double quantity, double tradePrice) {
        double totalCost = tradePrice * quantity;
        var balanceSource = balance.get(source);

        if(balanceSource < totalCost) {
            throw new RuntimeException("Insufficient " + source + " balance");
        }

        balance.replace(source, balanceSource - totalCost);
        balance.replace(dest, balance.getOrDefault(dest, 0.0) + quantity);
        return balance;
    }

    private Map<String, Double> sell(String source, String dest, Map<String, Double> balance, double quantity,
                      double tradePrice) {
        double totalCost = tradePrice * quantity;
        var balanceSource = balance.get(source);

        if (quantity > balanceSource) {
            throw new RuntimeException("Insufficient " + source + " balance");
        }
        balance.replace(source, balanceSource - quantity);
        balance.replace(dest, balance.getOrDefault(dest, 0.0) + totalCost);
        return balance;
    }
}
