package com.aquariux.trading_platform.service;


import com.aquariux.trading_platform.entity.Transaction;
import com.aquariux.trading_platform.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getListTransaction(Long userId){
        return transactionRepository.findAllByUserIdOrderByTimestampDesc(userId);
    }

    public void insertTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
