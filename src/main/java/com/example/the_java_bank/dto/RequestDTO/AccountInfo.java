package com.example.the_java_bank.dto.RequestDTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountInfo {

    private String accountName;

    private BigDecimal accountBalance;

    private String accountNumber;

}
