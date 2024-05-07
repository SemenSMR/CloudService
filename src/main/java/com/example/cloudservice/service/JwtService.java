package com.example.cloudservice.service;

import com.example.cloudservice.config.UserDetailsImpl;
import com.example.cloudservice.exception.IllegalArgumentException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private static final String SECRET_KEY = "2baefee2225b20317e9e97b2a9644e2e2fd0ea5bb9519fe428c8a2b42c7a29af";

    public String extractUserName(String token) {
        try {
            String username = extractClaim(token, Claims::getSubject);
            logger.debug("Extracted username: {}", username);
            return username;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Long extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Object userIdObject = claims.get("userId");
            if (userIdObject != null) {
                return Long.parseLong(userIdObject.toString());
            } else {
                throw new IllegalArgumentException("Key 'userId' is missing or its value is null in the JWT token.");
            }
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("Unable to extract user ID from JWT token. The token may be malformed or expired.", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to extract user ID from JWT token.", e);
        }
    }

    public String generateToken(UserDetailsImpl userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", userDetails.getId());
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetailsImpl userDetails) {
        String token = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInkey(), SignatureAlgorithm.HS256)
                .compact();


        return token;
    }

    public boolean istokenValid(String token, UserDetailsImpl userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !istokenExpired(token);
    }

    private boolean istokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        String tokenWithoutPrefix = token.replace("Bearer ", "");
        return Jwts
                .parser()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJws(tokenWithoutPrefix)
                .getBody();


    }

    private Key getSignInkey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
