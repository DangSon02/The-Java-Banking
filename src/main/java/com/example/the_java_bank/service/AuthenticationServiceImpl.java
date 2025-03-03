package com.example.the_java_bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.the_java_bank.dto.RequestDTO.SignInRequest;
import com.example.the_java_bank.dto.ResponseDTO.TokenResponse;
import com.example.the_java_bank.exception.InvalidDataException;
import com.example.the_java_bank.repository.UserRepository;
import com.example.the_java_bank.service.impl.JwtService;
import com.example.the_java_bank.service.impl.UserService;
import com.example.the_java_bank.utils.TokenType;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationServiceImpl {

        @Autowired
        UserRepository userRepository;

        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        JwtService jwtService;

        @Autowired
        UserService userService;

        public TokenResponse signIn(SignInRequest signInRequest) {

                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
                                                signInRequest.getPassword()));

                var user = userRepository.findByEmail(signInRequest.getEmail())
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "User name or Password are not connect"));

                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                return TokenResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .userId(user.getId())
                                .build();

        }

        public TokenResponse refreshToken(HttpServletRequest request) {
                final String refreshToken = request.getHeader("x-token");

                if (StringUtils.isBlank(refreshToken)) {

                        throw new InvalidDataException("Not allow access with this token");
                }

                // extract username;

                final String email = jwtService.extractUserName(refreshToken, TokenType.REFRESH_TOKEN);

                var user = userService.getUserByEmail(email);

                if (!jwtService.isTokenValid(refreshToken, TokenType.REFRESH_TOKEN, user)) {
                        throw new InvalidDataException("Not allow access with this token");
                }

                String accessToken = jwtService.generateAccessToken(user);

                return TokenResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .userId(user.getId())
                                .build();
        }

}
