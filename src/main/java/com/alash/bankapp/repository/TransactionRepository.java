package com.alash.bankapp.repository;

import com.alash.bankapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    boolean existsByAccountNumber(String accountNumber);
    List<Transaction> findByAccountNumber(String accountNumber);
}
