package com.example.Kiddit.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.security.Key;

@Service
public class JwtUtil {

    private Key secretKey;

    public JwtUtil() {
        try {
            // Generate a secret key for signing JWT tokens using HMACSHA256 algorithm
            javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance("HmacSHA256");
            keyGenerator.init(256); // Set key size
            this.secretKey = keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate secret key", e);
        }
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
        long expirationTime = 60 * 60 * 1000; // 1 hour expiration

        return Jwts.builder()
                .claim("sub", String.valueOf(userId)) // User ID claim
                .claim("firstName", firstName) // User first name claim
                .claim("lastName", lastName) // User last name claim
                .setIssuedAt(new Date()) // Token issue time
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Token expiration time
                .signWith(secretKey) // Sign the token with the secret key
                .compact();
    }

    /**
     * Extracts the claims from the given JWT token.
     * 
     * @param token the JWT token
     * @return the claims in the token
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey) // Set the secret key for validation
                .build()
                .parseClaimsJws(token) // Parse the token
                .getPayload(); // Return the claims
    }

    /**
     * Extracts the user ID from the given JWT token.
     * 
     * @param token the JWT token
     * @return the user ID contained in the token
     */
    public Long extractUserId(String token) {
        return Long.parseLong(extractClaims(token).getSubject()); // Get user ID from the subject claim
    }

    /**
     * Checks if the given JWT token has expired.
     * 
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date()); // Check if the expiration date is in the past
    }

    /**
     * Validates the given JWT token by checking the user ID and expiration date.
     * 
     * @param token the JWT token
     * @param userId the expected user ID
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token, Long userId) {
        return (userId.equals(extractUserId(token)) && !isTokenExpired(token)); // Validate token based on user ID and expiration
    }
}
