package com.ibgs.studyAssistant.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key key;

    @Value("${jwt.expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(String username) {
        return createToken(username, accessExpiration, "ACCESS");
    }

    public String generateRefreshToken(String username) {
        return createToken(username, refreshExpiration, "REFRESH");
    }

    private String createToken(String subject, Long expiration, String type) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("type", type)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isAccessToken(String token) {
        return "ACCESS".equals(extractTokenType(token));
    }

    public boolean isRefreshToken(String token) {
        return "REFRESH".equals(extractTokenType(token));
    }

    public boolean validateAccessToken(String token, String username) {
        return isAccessToken(token)
                && extractUsername(token).equals(username)
                && !isTokenExpired(token);
    }

    public boolean validateRefreshToken(String token) {
        return isRefreshToken(token) && !isTokenExpired(token);
    }
}
