package com.example.Kiddit.Service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Filters incoming requests to check for a valid JWT and sets authentication in the security context.
     * 
     * @param request the HTTP request to process
     * @param response the HTTP response to write to
     * @param filterChain the filter chain to pass the request and response along
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(@org.springframework.lang.NonNull HttpServletRequest request, 
                                    @org.springframework.lang.NonNull HttpServletResponse response, 
                                    @org.springframework.lang.NonNull FilterChain filterChain)
                                throws ServletException, IOException {

        // Extract the JWT from the Authorization header
        String jwt = extractJwtFromRequest(request);

        if (jwt != null && jwtUtil.validateToken(jwt, jwtUtil.extractUserId(jwt))) {
            Long userId = extractUserIdFromJwt(jwt);
            // Create authentication object and set it into the SecurityContext
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userId, null, null); // Set user details and authorities here if needed
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set the authentication object into the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT from the "Authorization" header in the HTTP request.
     *
     * @param request the HTTP request containing the Authorization header
     * @return the JWT token if present, null otherwise
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    private Long extractUserIdFromJwt(String jwt) {
        return jwtUtil.extractUserId(jwt);
    }
}
