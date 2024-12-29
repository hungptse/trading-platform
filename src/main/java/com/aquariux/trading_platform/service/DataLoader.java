package com.aquariux.trading_platform.service;

import com.aquariux.trading_platform.entity.UserWallet;
import com.aquariux.trading_platform.repository.UserWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final UserWalletRepository userWalletRepository;

    @Autowired
    public DataLoader(UserWalletRepository userWalletRepository) {
        this.userWalletRepository = userWalletRepository;
    }

    public void run(ApplicationArguments args) {
        userWalletRepository.save(new UserWallet(1000L, "hungpt", "{\"USDT\": 50000.0, \"BTC\": 0.0, \"ETH\": 0.0}"));
    }
}