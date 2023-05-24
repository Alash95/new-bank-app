package com.alash.bankapp.service.impl;

import com.alash.bankapp.dto.TransactionDto;
import com.alash.bankapp.entity.Transaction;
import com.alash.bankapp.entity.User;
import com.alash.bankapp.repository.TransactionRepository;
import com.alash.bankapp.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }


    @Override
    public void saveTransaction(TransactionDto transaction) {

        Transaction newTransaction = Transaction.builder()
                .transactionType(transaction.getTransactionType())
                .accountNumber(transaction.getAccountNumber())
                .amount(transaction.getAmount())
                .build();

         transactionRepository.save(newTransaction);

    }

    @Override
    public List<TransactionDto> fetchAllTransactions(User user) {
        List<Transaction> transactionList = transactionRepository.findAll();

        List<TransactionDto> transactionDto = new ArrayList<>();
        for (Transaction transaction: transactionList) {
            transactionDto.add(TransactionDto.builder()
                        .transactionType(transaction.getTransactionType())
                        .accountNumber(transaction.getAccountNumber())
                        .amount(transaction.getAmount())
                    .build());
        }
        return transactionDto;
    }

    @Override
    public List<TransactionDto> fetchSingleUserTransaction(String accountNumber, String debitOrCredit) {

        List<Transaction> transactionList =  transactionRepository.findByAccountNumber(accountNumber);

        if (!transactionRepository.existsByAccountNumber(accountNumber)){
            return null;
        }

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for (Transaction transaction: transactionList){
            if (transaction.getTransactionType().equalsIgnoreCase(debitOrCredit)){
                TransactionDto transactionDto = TransactionDto.builder()
                        .transactionType(transaction.getTransactionType())
                        .accountNumber(transaction.getAccountNumber())
                        .amount(transaction.getAmount())
                        .build();
                transactionDtoList.add(transactionDto);
            }
        }
        return transactionDtoList;
    }
}
