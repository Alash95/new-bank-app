package com.alash.bankapp.service;

import com.alash.bankapp.dto.*;

import java.util.List;

public interface UserService {

    Response registerUser(UserRequest userRequest);
    List<Response> allUsers();
    Response fetchUser(Long userId) throws Exception;
    Response balanceEnquiry(String accountNumber);
    Response nameEnquiry(String accountNumber);
    Response credit(TransactionRequest transactionRequest);
    Response debit(TransactionRequest transactionRequest);
    List<TransactionDto> fetchAllTransactionsByUser(String accountNumber);
    Response transfer(TransferRequest transferRequest);
}
