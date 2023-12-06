package com.sq018.monieflex.configs;

import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.services.implementations.JwtImplementation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtFilterConfiguration extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtImplementation jwtImplementation;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer")){
            doFilter(request, response, filterChain);
            return;
        }
        String jwtToken = header.substring(7);
        String email = jwtImplementation.extractEmailAddressFromToken(jwtToken);
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails details = userDetailsService.loadUserByUsername(email);
            if(jwtImplementation.isValid(jwtToken, details)) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        details, null
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            } else {
                throw new MonieFlexException("Your session has expired. Please login");
            }
        }
        doFilter(request, response, filterChain);

    }
}
