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
public interface TokenService {

    /**
     * Genera un token JWT con el email del usuario.
     *
     * @param email el email del usuario
     * @return el token generado
     */
    public String generateToken(String email);

    /**
     * Valida un token JWT.
     *
     * @param token el token a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token);

    /**
     * Extrae el email del token JWT.
     *
     * @param token el token JWT
     * @return el email del usuario
     */
    public String extractEmailFromToken(String token);
}
