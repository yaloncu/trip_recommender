package com.backend.mybackend.serviceTest;

import com.example.mybackend.services.impl.TokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceImplTest {

    private TokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenServiceImpl();
    }

    @Test
    void testGenerateAndValidateToken() {
        String email = "test@example.com";
        String token = tokenService.generateToken(email);

        assertNotNull(token);
        assertTrue(tokenService.validateToken(token));
    }

    @Test
    void testExtractEmailFromToken() {
        String email = "test@example.com";
        String token = tokenService.generateToken(email);

        String extractedEmail = tokenService.extractEmailFromToken(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    void testValidateInvalidToken() {
        String invalidToken = "invalid.token.string";

        boolean result = tokenService.validateToken(invalidToken);
        assertFalse(result);
    }
}
