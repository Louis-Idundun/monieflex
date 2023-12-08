package com.sq018.monieflex.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractEmailAddressFromToken(String token);
    String generateJwtToken(Map<String, Object> claims, String emailAddress);
    Boolean isValid(String token, UserDetails userDetails);
    Boolean isExpired(String token);
}
