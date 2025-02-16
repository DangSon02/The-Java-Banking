package com.example.the_java_bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.the_java_bank.dto.TransactionDTO;
import com.example.the_java_bank.entity.Transaction;
import com.example.the_java_bank.repository.TransactionRepository;
import com.example.the_java_bank.service.impl.TransactionService;

@Component
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = Transaction.builder()
                .accountNumber(transactionDTO.getAccountNumber())
                .amount(transactionDTO.getAmount())
                .transactionType(transactionDTO.getTransactionType())
                .status("SUCESS")
                .build();

        transactionRepository.save(transaction);

    }

}
