package com.example.the_java_bank.service.impl;

import com.example.the_java_bank.dto.BankResponse;
import com.example.the_java_bank.dto.CreditRequest;
import com.example.the_java_bank.dto.DebitRequest;
import com.example.the_java_bank.dto.TransferRequest;
import com.example.the_java_bank.dto.UserRequest;

public interface UserService {

    BankResponse creatAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(String accountNumber);

    String nameEnquiry(String accountNumber);

    BankResponse credit(String accountNumber, CreditRequest creditDebitRequest);

    BankResponse debit(String accountNumber, DebitRequest debitRequest);

    BankResponse transfer(TransferRequest transferRequest);

}
