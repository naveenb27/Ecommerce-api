package com.backend.backend.utils;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class validate {

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getSecretKey() {
        System.out.println("Secret Key: " + secretKey);
        try {
            System.out.println("Secret key in bytes: " + Keys.hmacShaKeyFor(secretKey.getBytes()));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Claims validateToken(String token) {
        try {
            System.out.println("Entered");
            Claims ans =  Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("Claims : "+ans);

            return ans;
        } catch (Exception e) {
            System.out.println("Invalid JWT Token: " + e.getMessage());
            return null;
        }
    }
}
