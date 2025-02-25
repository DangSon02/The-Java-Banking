package com.example.the_java_bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.the_java_bank.dto.RequestDTO.SignInRequest;
import com.example.the_java_bank.dto.ResponseDTO.SignInResponse;
import com.example.the_java_bank.repository.UserRepository;
import com.example.the_java_bank.service.impl.JwtService;

@Service
public class AuthenticationServiceImpl {

        @Autowired
        UserRepository userRepository;

        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        JwtService jwtService;

        public SignInResponse signIn(SignInRequest signInRequest) {

                System.out.println("ok1");
                System.out.println("email" + signInRequest.getEmail());
                System.out.println("pass" + signInRequest.getPassword());

                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
                                                signInRequest.getPassword()));

                System.out.println("ok2");

                var user = userRepository.findByEmail(signInRequest.getEmail())
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "User name or Password are not connect"));

                System.out.println("ok3");

                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                System.out.println("ok4");

                return SignInResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .userId(user.getId())
                                .build();

        }

}
