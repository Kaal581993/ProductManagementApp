package com.productandordermanagementsystem.backend.user_service.security;

import com.productandordermanagementsystem.backend.user_service.config.JwtProperties;
import com.productandordermanagementsystem.backend.user_service.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.expirationMs());
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(expiry)
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .claim("department", user.getDepartment())
                .claim("region", user.getRegion())
                .signWith(signingKey())
                .compact();
    }

    public String extractUsername(String token) {

        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token)
                .equals(userDetails.getUsername()) &&
                !extractClaims(token)
                        .getExpiration()
                        .before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }
}
