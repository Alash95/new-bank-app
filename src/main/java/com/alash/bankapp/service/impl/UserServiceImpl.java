package com.alash.bankapp.service.impl;

import com.alash.bankapp.dto.*;
import com.alash.bankapp.entity.User;
import com.alash.bankapp.repository.UserRepository;
import com.alash.bankapp.service.TransactionService;
import com.alash.bankapp.service.UserService;
import com.alash.bankapp.utils.ResponseUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TransactionService transactionService;

    public UserServiceImpl(UserRepository userRepository, TransactionService transactionService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
    }

    @Override
    public Response registerUser(UserRequest userRequest) {
        /**
         * check if user exists, if the user doesn't exist return response,
         * generate account number
         * if not, go ahead to save the user
         */
        boolean isEmailExist = userRepository.existsByEmail(userRequest.getEmail());
        if (isEmailExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtils.USER_EXISTS_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(ResponseUtils.generateAccountNumber(ResponseUtils.LENGTH_OF_ACCOUNT_NUMBER))
                .accountBalance(userRequest.getAccountBalance())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .dateOfBirth(userRequest.getDateOfBirth())
                .build();


        User savedUser = userRepository.save(user);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.USER_REGISTERED_SUCCESS)
                .data(Data.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public List<Response> allUsers() {
        List<User> usersList = userRepository.findAll();

        List<Response> response = new ArrayList<>();
        for (User user : usersList) {
            response.add(Response.builder()
                    .responseCode(ResponseUtils.SUCCESS)
                    .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                    .data(Data.builder()
                            .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                            .accountNumber(user.getAccountNumber())
                            .accountBalance(user.getAccountBalance())
                            .build())
                    .build());
        }
        return response;
    }

    @Override
    public Response fetchUser(Long userId) throws Exception {

        if (!userRepository.existsById(userId)) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND)
                    .data(null)
                    .build();
        }
        User user = userRepository.findById(userId).get();

        return Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.SUCCESS)
                .data(Data.builder()
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public Response balanceEnquiry(String accountNumber) {
        /**
         * check if accNum Exists
         * return the balance info
         */

        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND)
                    .data(null)
                    .build();
        }

        User user = userRepository.findByAccountNumber(accountNumber);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .build())
                .build();
    }

    @Override
    public Response nameEnquiry(String accountNumber) {

        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND)
                    .data(null)
                    .build();
        }

        User user = userRepository.findByAccountNumber(accountNumber);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(null)
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .build())
                .build();
    }

    @Override
    public Response credit(TransactionRequest transactionRequest) {
        /**
         * add amount to current accountbalance
         */
        User receivingUser = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());

        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND)
                    .data(null)
                    .build();
        }

        receivingUser.setAccountBalance(receivingUser.getAccountBalance().add(transactionRequest.getAmount()));
        userRepository.save(receivingUser);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType("Credit");
        transactionDto.setAccountNumber(transactionRequest.getAccountNumber());
        transactionDto.setAmount(transactionRequest.getAmount());

        transactionService.saveTransaction(transactionDto);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.ACCOUNT_CREDITED)
                .data(Data.builder()
                        .accountNumber(transactionRequest.getAccountNumber())
                        .accountBalance(receivingUser.getAccountBalance())
                        .accountName(receivingUser.getFirstName() + " " + receivingUser.getLastName() + " " + receivingUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public Response debit(TransactionRequest transactionRequest) {
        User debitedUser = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());

        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND)
                    .data(null)
                    .build();
        }

        if (debitedUser.getAccountBalance().compareTo(transactionRequest.getAmount()) > 0) {

            debitedUser.setAccountBalance(debitedUser.getAccountBalance().subtract(transactionRequest.getAmount()));
            userRepository.save(debitedUser);

            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setTransactionType("Debit");
            transactionDto.setAccountNumber(transactionRequest.getAccountNumber());
            transactionDto.setAmount(transactionRequest.getAmount());

            transactionService.saveTransaction(transactionDto);


            return Response.builder()
                    .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                    .responseMessage(ResponseUtils.ACCOUNT_DEBITED)
                    .data(Data.builder()
                            .accountNumber(transactionRequest.getAccountNumber())
                            .accountBalance(debitedUser.getAccountBalance())
                            .accountName(debitedUser.getFirstName() + " " + debitedUser.getLastName() + " " + debitedUser.getOtherName())
                            .build())
                    .build();
        }
        return Response.builder()
                .responseCode(ResponseUtils.UNSUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.INSUFFICIENT_FUNDS)
                .data(null)
                .build();
    }

    @Override
    public List<TransactionDto> fetchAllTransactionsByUser(String accountNumber) {
        User user = userRepository.findByAccountNumber(accountNumber);
        return transactionService.fetchAllTransactions(user);
    }

    @Override
    public Response transfer(TransferRequest transferRequest) {
        User sourceAccount = userRepository.findByAccountNumber(transferRequest.getSendingAccountNumber());
        User destinationAccount = userRepository.findByAccountNumber(transferRequest.getReceivingAccountNumber());

        if (!userRepository.existsByAccountNumber(transferRequest.getSendingAccountNumber()) ||
                !userRepository.existsByAccountNumber(transferRequest.getReceivingAccountNumber())) {

            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND)
                    .data(null)
                    .build();
        }
        if (sourceAccount.getAccountBalance().compareTo(transferRequest.getAmount()) > 0) {
            sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(transferRequest.getAmount()));
            userRepository.save(sourceAccount);

            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setTransactionType("Debit");
            transactionDto.setAccountNumber(transferRequest.getSendingAccountNumber());
            transactionDto.setAmount(transferRequest.getAmount());

            transactionService.saveTransaction(transactionDto);

            destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(transferRequest.getAmount()));
            userRepository.save(destinationAccount);

            TransactionDto transactionDto1 = new TransactionDto();
            transactionDto1.setTransactionType("Credit");
            transactionDto1.setAccountNumber(transferRequest.getReceivingAccountNumber());
            transactionDto1.setAmount(transferRequest.getAmount());

            transactionService.saveTransaction(transactionDto1);

            return Response.builder()
                    .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                    .responseMessage(ResponseUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                    .data(Data.builder()
                            .accountNumber(transferRequest.getSendingAccountNumber())
                            .accountName(sourceAccount.getFirstName() + " " + sourceAccount.getLastName() + " " + sourceAccount.getOtherName())
                            .accountBalance(sourceAccount.getAccountBalance())
                            .accountNumber(transferRequest.getReceivingAccountNumber())
                            .accountName(destinationAccount.getFirstName() + " " + destinationAccount.getLastName() + " " + destinationAccount.getOtherName())
                            .accountBalance(destinationAccount.getAccountBalance())
                            .build())
                    .build();
        }
        return Response.builder()
                .responseCode(ResponseUtils.UNSUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.INSUFFICIENT_FUNDS)
                .data(null)
                .build();


    }


}
