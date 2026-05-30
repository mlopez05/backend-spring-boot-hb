package com.umg.microservicios.auth_service.servicio;

import java.util.Date;
import java.util.HashMap;
import java.security.Key;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import com.umg.microservicios.auth_service.modelo.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String SECRET_KEY = "u8K9m1vQ2sR7xZ3cP0aB5dE6fG8hI2jL9nM4oP7qR1sT2uV5wX8yZ0aBcDeFgH1iJ";

    public String getToken(UserDetails usuario) {
        Map<String, Object> extraClaims = new HashMap<>();

        if (usuario instanceof Usuario u && u.getRol() != null) {
            extraClaims.put("rol", u.getRol().getNombre());
            extraClaims.put("userId", u.getId());
        }

        return getToken(extraClaims, usuario);
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails usuario) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(usuario.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(getKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String usuario = getUsernameFromToken(token);
        return (usuario.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}