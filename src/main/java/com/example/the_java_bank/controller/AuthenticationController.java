package com.example.the_java_bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.the_java_bank.dto.RequestDTO.SignInRequest;
import com.example.the_java_bank.dto.ResponseDTO.TokenResponse;
import com.example.the_java_bank.service.AuthenticationServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    public AuthenticationController(AuthenticationServiceImpl authenticationServiceImpl) {
        this.authenticationServiceImpl = authenticationServiceImpl;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> signIn(@RequestBody SignInRequest signInRequest) {

        return new ResponseEntity<>(authenticationServiceImpl.signIn(signInRequest), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {

        return new ResponseEntity<>(authenticationServiceImpl.refreshToken(request), HttpStatus.OK);

    }

    @PostMapping("/logout")
    public String logout(String userName, String password) {

        return "sucess";

    }

}
