package com.backend.backend.Config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.backend.backend.Model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getSecretKey() {
        try {
            return Keys.hmacShaKeyFor(secretKey.getBytes());
        } catch (Exception e) {
            logger.error("Error generating secret key", e);
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("googleId", user.getGoogleId());
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("picture", user.getPicture());

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(user.getGoogleId())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days
            .signWith(getSecretKey())
            .compact();
    }

    public Claims validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (claims.getExpiration().before(new Date())) {
                logger.warn("Token is expired");
                return null;
            }

            return claims;
        } catch (Exception e) {
            logger.error("Invalid JWT Token: " + e.getMessage());
            return null;
        }
    }
}
