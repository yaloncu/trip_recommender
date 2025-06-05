package com.backend.mybackend.serviceTest;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.repository.GroupRepository;
import com.example.mybackend.repository.UserRepository;
import com.example.mybackend.services.impl.Neo4jUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class Neo4jUserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private Neo4jUserServiceImpl userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .email("test@example.com")
                .name("Test User")
                .password("password123")
                .build();
    }

    @Test
    void testCreateUser_withPassword() {
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.createUser("Test User", "test@example.com", "encodedPassword")).thenReturn(mockUser);

        User result = userService.createUser("Test User", "test@example.com", "password123");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).createUser("Test User", "test@example.com", "encodedPassword");
    }

    @Test
    void testCreateUser_withoutPassword() {
        when(userRepository.createUser("Test User", "test@example.com")).thenReturn(mockUser);

        User result = userService.createUser("Test User", "test@example.com", null);

        assertNotNull(result);
        verify(userRepository).createUser("Test User", "test@example.com");
    }

    @Test
    void testCheckUserExistsByEmail() {
        when(userRepository.checkUserExists("test@example.com")).thenReturn(true);

        boolean exists = userService.checkUserExistsByEmail("test@example.com");

        assertTrue(exists);
        verify(userRepository).checkUserExists("test@example.com");
    }

    @Test
    void testValidateUser_correctPassword() {
        when(userRepository.validateUser("test@example.com", "password123")).thenReturn(true);

        boolean isValid = userService.validateUser("test@example.com", "password123");

        assertTrue(isValid);
        verify(userRepository).validateUser("test@example.com", "password123");
    }

    @Test
    void testValidateUser_wrongPassword() {
        when(userRepository.validateUser("test@example.com", "wrongpass")).thenReturn(false);

        boolean isValid = userService.validateUser("test@example.com", "wrongpass");

        assertFalse(isValid);
        verify(userRepository).validateUser("test@example.com", "wrongpass");
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        User result = userService.getUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testGetUserGroups() {
        Group mockGroup = Group.builder()
                .name("Test Group")
                .email("test@example.com")
                .departureDate(LocalDate.of(2025, 6, 1))
                .returnDate(LocalDate.of(2025, 6, 10))
                .build();

        when(userRepository.findUserGroups("test@example.com")).thenReturn(List.of(mockGroup));

        List<Group> result = userService.getUserGroups("test@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Group", result.get(0).getName());
        verify(userRepository).findUserGroups("test@example.com");
    }
}
