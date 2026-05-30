package com.umg.microservicios.historial_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
 
import java.security.Key;

@Component
public class JwtUtil {

     private static final String SECRET_KEY =
            "u8K9m1vQ2sR7xZ3cP0aB5dE6fG8hI2jL9nM4oP7qR1sT2uV5wX8yZ0aBcDeFgH1iJ";
 
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
 
    public Claims getClaims(String authHeader) {
        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7) : authHeader;
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
 
    public String getUsername(String authHeader) {
        return getClaims(authHeader).getSubject();
    }
}
