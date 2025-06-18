package com.backend.mybackend.serviceTest;

import com.example.mybackend.model.User;
import com.example.mybackend.repository.UserRepository;
import com.example.mybackend.services.impl.CustomUserDetailsServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServicesImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsServicesImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    public void testLoadUserByUsername_userExists() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("password123");

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        UserDetails result = userDetailsService.loadUserByUsername("user@example.com");

        assertNotNull(result);
        assertEquals("user@example.com", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertTrue(result.getAuthorities().isEmpty()); // <-- solución aquí
    }

    @Test
    public void testLoadUserByUsername_userNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("notfound@example.com");
        });
    }
}
