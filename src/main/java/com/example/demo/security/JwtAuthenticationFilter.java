package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Stub filter: checks for any non-empty Bearer token and considers it "authenticated".
 * Replace `validateToken(...)` with real JWT validation.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // TODO: replace this stub with actual JWT validation logic:
            if (validateToken(token)) {
                // If valid, set an “authenticated” principal (no authorities)
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(token, null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

    private boolean validateToken(String token) {
        // In a real implementation, parse/verify the JWT signature & expiration.
        // For now, any non-empty token is accepted.
        return !token.trim().isEmpty();
    }
}
