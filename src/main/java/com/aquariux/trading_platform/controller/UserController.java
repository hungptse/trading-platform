package com.aquariux.trading_platform.controller;

import com.aquariux.trading_platform.model.UserBalance;
import com.aquariux.trading_platform.service.UserWalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserWalletService userWalletService;

    public UserController(UserWalletService userWalletService) {
        this.userWalletService = userWalletService;
    }

    @GetMapping("/balance")
    public UserBalance getBalance(@RequestHeader("TF-USER") long userId){
        return userWalletService.getBalance(userId);
    }
}
