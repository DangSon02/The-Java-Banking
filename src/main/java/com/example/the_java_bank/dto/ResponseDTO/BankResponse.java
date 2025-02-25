package com.example.the_java_bank.dto.ResponseDTO;

import com.example.the_java_bank.dto.RequestDTO.AccountInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankResponse {

    private String responseCode;

    private String responseMessage;

    private AccountInfo accountInfo;

}
