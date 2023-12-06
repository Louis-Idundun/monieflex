package com.sq018.monieflex.services.implementations;

import com.sq018.monieflex.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtImplementation implements JwtService {
    @Value("${monieFlex.security.jwt-secret-key}")
    private String JWT_SECRET_KEY;

    protected Key signingKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET_KEY));
    }

    protected Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    protected <T> T extractClaims(String token, Function<Claims, T> extract) {
        return extract.apply(getAllClaims(token));
    }

    @Override
    public String extractEmailAddressFromToken(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    @Override
    public String generateJwtToken(Map<String, Object> claims, String emailAddress) {
        return Jwts
                .builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .setClaims(claims)
                .setSubject(emailAddress)
                .compact();
    }

    @Override
    public Boolean isValid(String token, UserDetails userDetails) {
        return extractEmailAddressFromToken(token).equals(userDetails.getUsername()) && !isExpired(token);
    }

    @Override
    public Boolean isExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }
}
