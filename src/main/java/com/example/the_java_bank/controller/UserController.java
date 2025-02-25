package com.example.the_java_bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.the_java_bank.dto.RequestDTO.CreditRequest;
import com.example.the_java_bank.dto.RequestDTO.DebitRequest;
import com.example.the_java_bank.dto.RequestDTO.TransferRequest;
import com.example.the_java_bank.dto.RequestDTO.UserRequest;
import com.example.the_java_bank.dto.ResponseDTO.BankResponse;
import com.example.the_java_bank.service.impl.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping()
    public BankResponse creatAccount(@RequestBody UserRequest userRequest) {

        return userService.creatAccount(userRequest);

    }

    @GetMapping("/balance/{accountNumber}")
    public BankResponse balanceEnquiry(@PathVariable String accountNumber) {
        return userService.balanceEnquiry(accountNumber);
    }

    @GetMapping("/name/{accountNumber}")
    public String nameEnquiry(@PathVariable String accountNumber) {
        return userService.nameEnquiry(accountNumber);
    }

    @PostMapping("/credit/{accountNumber}")
    public BankResponse credit(@PathVariable String accountNumber,
            @RequestBody CreditRequest creditDebitRequest) {
        return userService.credit(accountNumber, creditDebitRequest);
    }

    @PostMapping("/debit/{accountNumber}")
    public BankResponse debit(@PathVariable String accountNumber,
            @RequestBody DebitRequest debitRequest) {
        return userService.debit(accountNumber, debitRequest);
    }

    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest) {
        return userService.transfer(transferRequest);
    }

}
