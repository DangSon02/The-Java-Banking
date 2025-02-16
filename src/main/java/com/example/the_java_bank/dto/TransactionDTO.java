package com.example.the_java_bank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDTO {

    private String transactionType;

    private BigDecimal amount;

    private String accountNumber;

    private String status;

}
