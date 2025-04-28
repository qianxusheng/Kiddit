package com.example.Kiddit.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    private final Key secretKey;
    private static final long EXPIRATION_TIME = 1000 * 36 * 36 * 24; // 1 day

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generates a JWT token for the user with the given details.
     *
     * @param userId the ID of the user
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @return the generated JWT token
     */
    public String generateToken(Long userId, String firstName, String lastName) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("firstName", firstName)
                .claim("lastName", lastName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @return the claims
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts the user ID (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the user ID
     */
    public Long extractUserId(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    /**
     * Checks if the token is expired.
     *
     * @param token the JWT token
     * @return true if expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * Validates the token by checking the user ID and expiration.
     *
     * @param token the JWT token
     * @param userId the expected user ID
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token, Long userId) {
        return userId.equals(extractUserId(token)) && !isTokenExpired(token);
    }
}
