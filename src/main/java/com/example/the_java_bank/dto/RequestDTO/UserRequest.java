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
public class UserRequest {

    private String firstName;

    private String lastName;

    private String otherName;

    private String gender;

    private String address;

    private String stateOfOrigin;

    private String accountNumber;

    private BigDecimal accountBalance;

    private String email;

    private String password;

    private String phoneNumber;

    private String alternativePhoneNumber;

    private String status;

}
