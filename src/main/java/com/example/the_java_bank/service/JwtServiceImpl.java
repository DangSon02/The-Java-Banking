package com.example.the_java_bank.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.the_java_bank.exception.InvalidDataException;
import com.example.the_java_bank.service.impl.JwtService;
import com.example.the_java_bank.utils.TokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access-secret-key}")
    private String acSecretKey;

    @Value("${jwt.fresh-secret-key}")
    private String rfSecretKey;

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
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)// thuat toan ma hoa
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> clams, UserDetails userDetails) {

        return Jwts.builder()
                .setClaims(clams) // nhung thong tin trong payload, khong muon public ra ngoai
                .setSubject(userDetails.getUsername())// tranh trung lap
                .setIssuedAt(new Date(System.currentTimeMillis()))// ngay tao ra token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))// thoi gian het token (24h)
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)// thuat toan ma hoa
                .compact();
    }

    // ma hoa secretKey
    private Key getKey(TokenType type) {

        byte[] keyBytes;

        if (type.equals(TokenType.ACCESS_TOKEN)) {
            keyBytes = Decoders.BASE64.decode(acSecretKey);
        } else {
            keyBytes = Decoders.BASE64.decode(rfSecretKey);
        }

        return Keys.hmacShaKeyFor(keyBytes);

    }

    @Override
    public String extractUserName(String token, TokenType type) {

        return extractClaim(token, type, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, TokenType type, UserDetails userDetails) {
        final String userName = extractUserName(token, type);

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token, type));

    }

    private Claims extractAllClaims(String token, TokenType type) {

        try {

            return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();

        } catch (Exception e) {
            throw new InvalidDataException("Invalid JWT token: " + e.getMessage());
        }

    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token, type);

        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date());
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaim(token, type, Claims::getExpiration);
    }

}
