package com.Jobs.JobsMemory.security;

import com.Jobs.JobsMemory.service.UserService;
import com.Jobs.JobsMemory.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userDetailsService;

    public JwtAuthenticationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        logger.info("Processing request: " + path);

        if (path.startsWith("/api/auth/")) {
            logger.info("AUTH ENDPOINT: Permitindo acesso sem validação JWT");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        logger.info("Auth header: " + authHeader);

        filterChain.doFilter(request, response);
    }
}
