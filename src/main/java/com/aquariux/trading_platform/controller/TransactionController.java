package com.aquariux.trading_platform.controller;


import com.aquariux.trading_platform.entity.Transaction;
import com.aquariux.trading_platform.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public List<Transaction> getTransaction(@RequestHeader("TF-USER") long userId) {
        return transactionService.getListTransaction(userId);
    }
}
