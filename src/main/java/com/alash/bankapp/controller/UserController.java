package com.alash.bankapp.controller;

import com.alash.bankapp.dto.*;
import com.alash.bankapp.entity.User;
import com.alash.bankapp.service.TransactionService;
import com.alash.bankapp.service.UserService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@OpenAPIDefinition(
        info = @Info(
                title = "Spring boot banking application",
                description = "Spring boot Banking Application REST APIs Implementation",
                version = "v1.0",
                contact = @Contact(
                        name = "Oyin",
                        email = "oyinlolaalasho@gmail.com",
                        url = "https://github.com/Alash95"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://github.com/Alash95"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring boot banking implementation",
                url = "https://github.com/Alash95"
        )
)
@Tag(
        name = "User account service REST APIs/Endpoint",
        description = "Endpoints for manipulating user Account"
)
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
