package com.alash.bankapp.service;

import com.alash.bankapp.dto.TransactionDto;
import com.alash.bankapp.entity.User;

import java.util.List;

public interface TransactionService {

    void saveTransaction(TransactionDto transaction);
    List<TransactionDto> fetchAllTransactions(User user);
    List<TransactionDto> fetchSingleUserTransaction(String accountNumber, String debitOrCredit);
}
