package com.example.the_java_bank.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.the_java_bank.service.impl.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")

    private String secretKey;

    @Override
    public String generateAccessToken(UserDetails userDetails) {

        return generateAcessToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    private String generateAcessToken(Map<String, Object> clams, UserDetails userDetails) {

        return Jwts.builder()
                .setClaims(clams) // nhung thong tin trong payload, khong muon public ra ngoai
                .setSubject(userDetails.getUsername())// tranh trung lap
                .setIssuedAt(new Date(System.currentTimeMillis()))// ngay tao ra token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))// thoi gian het token (1h)
                .signWith(getKey(), SignatureAlgorithm.HS256)// thuat toan ma hoa
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> clams, UserDetails userDetails) {

        return Jwts.builder()
                .setClaims(clams) // nhung thong tin trong payload, khong muon public ra ngoai
                .setSubject(userDetails.getUsername())// tranh trung lap
                .setIssuedAt(new Date(System.currentTimeMillis()))// ngay tao ra token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))// thoi gian het token (24h)
                .signWith(getKey(), SignatureAlgorithm.HS256)// thuat toan ma hoa
                .compact();
    }

    // ma hoa secretKey
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);

    }

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);

        return userName.equals(userDetails.getUsername());

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJwt(token).getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

}
