package com.example.the_java_bank.service.impl;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.the_java_bank.dto.RequestDTO.CreditRequest;
import com.example.the_java_bank.dto.RequestDTO.DebitRequest;
import com.example.the_java_bank.dto.RequestDTO.TransferRequest;
import com.example.the_java_bank.dto.RequestDTO.UserRequest;
import com.example.the_java_bank.dto.ResponseDTO.BankResponse;
import com.example.the_java_bank.entity.User;

public interface UserService {

    UserDetailsService userDetailsService();

    BankResponse creatAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(String accountNumber);

    String nameEnquiry(String accountNumber);

    BankResponse credit(String accountNumber, CreditRequest creditDebitRequest);

    BankResponse debit(String accountNumber, DebitRequest debitRequest);

    BankResponse transfer(TransferRequest transferRequest);

    User getUserByEmail(String email);

}
