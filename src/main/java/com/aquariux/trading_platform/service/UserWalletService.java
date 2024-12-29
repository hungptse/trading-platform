package com.aquariux.trading_platform.service;

import com.aquariux.trading_platform.entity.UserWallet;
import com.aquariux.trading_platform.model.UserBalance;
import com.aquariux.trading_platform.repository.UserWalletRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserWalletService {


    private final UserWalletRepository userWalletRepository;

    public UserWalletService(UserWalletRepository userWalletRepository) {
        this.userWalletRepository = userWalletRepository;
    }

    public UserBalance getBalance(Long userId) {
        var userWallet = userWalletRepository.findById(userId);
        if (userWallet.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        var userBalance = new UserBalance();
        userBalance.setUserId(userId);
        try {
            var balance = new ObjectMapper().readValue(userWallet.get().getBalanceJSON(), HashMap.class);
            userBalance.setBalance(balance);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse balance JSON");
        }
        return userBalance;
    }

    public void updateBalance(Long userId, Map<String, Double> newBalance) {
        var userWallet = userWalletRepository.findById(userId).orElseThrow();
        try {
            userWallet.setBalanceJSON(new ObjectMapper().writeValueAsString(newBalance));
            userWalletRepository.save(userWallet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse balance JSON");
        }
    }
}
