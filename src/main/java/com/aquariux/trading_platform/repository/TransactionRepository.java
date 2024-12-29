package com.aquariux.trading_platform.repository;

import com.aquariux.trading_platform.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUserIdOrderByTimestampDesc(Long userId);
}
