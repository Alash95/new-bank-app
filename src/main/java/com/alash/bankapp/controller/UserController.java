package com.alash.bankapp.controller;

import com.alash.bankapp.dto.*;
import com.alash.bankapp.entity.User;
import com.alash.bankapp.service.TransactionService;
import com.alash.bankapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final TransactionService transactionService;

    public UserController(UserService userService, TransactionService transactionService){
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public Response registerUser(@RequestBody UserRequest userRequest){
        return userService.registerUser(userRequest);
    }

    @GetMapping
    public List<Response> fetchAllUsers(){
        return userService.allUsers();
    }

    @GetMapping("/{userId}")
    public Response fetchSingleUser(@PathVariable (name = "userId") Long userId) throws Exception {
        return userService.fetchUser(userId);
    }

    @GetMapping("/balEnquiry")
    public Response fetchByAccountNumber(@RequestParam (name = "accountNumber") String accountNumber) {
        return userService.balanceEnquiry(accountNumber);
    }

    @GetMapping("/nameEnquiry")
    public Response fetchByName(@RequestParam (name = "accountNumber") String accountNumber) {
        return userService.nameEnquiry(accountNumber);
    }

    @PutMapping("/credit")
    public Response credit(@RequestBody TransactionRequest transactionRequest) {
        return userService.credit(transactionRequest);
    }

    @PutMapping("/debit")
    public Response debit(@RequestBody TransactionRequest transactionRequest) {
        return userService.debit(transactionRequest);
    }


    @GetMapping("/allTransactions")
    public List<TransactionDto> fetchAllTransactionsByUser(@RequestBody String accountNumber) {
        return userService.fetchAllTransactionsByUser(accountNumber);
    }

    @PutMapping("/transfer")
    public Response transfer(@RequestBody TransferRequest transferRequest) {
        return userService.transfer(transferRequest);
    }

    @GetMapping("/{accountNumber}/type")
    public List<TransactionDto> fetchSingleUserTransaction(@PathVariable("accountNumber") String accountNumber, @RequestParam (name = "type") String debitOrCredit){
        return transactionService.fetchSingleUserTransaction(accountNumber, debitOrCredit);
    }

}
