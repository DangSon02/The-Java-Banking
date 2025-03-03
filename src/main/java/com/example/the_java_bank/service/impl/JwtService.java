package com.example.the_java_bank.service.impl;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.the_java_bank.utils.TokenType;

public interface JwtService {

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String extractUserName(String token, TokenType type);

    boolean isTokenValid(String token, TokenType type, UserDetails userDetails);

}