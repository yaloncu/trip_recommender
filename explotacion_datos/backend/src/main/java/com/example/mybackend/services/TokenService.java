package com.example.mybackend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 * TokenService is a service that provides token-related operations.
 */
@Service
public class TokenService {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTime = 3600000; // Tiempo de expiración en milisegundos (1 hora)

    /**
     * Genera un token JWT con el email del usuario.
     *
     * @param email el email del usuario
     * @return el token generado
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Valida un token JWT.
     *
     * @param token el token a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extrae el email del token JWT.
     *
     * @param token el token JWT
     * @return el email del usuario
     */
    public String extractEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
