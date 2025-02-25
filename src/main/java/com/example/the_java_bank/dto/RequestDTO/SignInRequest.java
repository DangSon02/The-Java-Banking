package com.example.the_java_bank.dto.RequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequest {

    private String email;

    private String password;

}
