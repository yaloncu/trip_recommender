package com.backend.mybackend.controllerTest;

import com.example.mybackend.controller.impl.UserControllerImpl;
import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.services.Neo4jUserService;
import com.example.mybackend.services.TokenService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserControllerImplTest {

    @Mock
    private Neo4jUserService userService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserControllerImpl controller;

    @Test
    void testProtectedRoute_validToken() {
        String token = "Bearer valid.jwt.token";

        when(tokenService.validateToken("valid.jwt.token")).thenReturn(true);
        when(tokenService.extractEmailFromToken("valid.jwt.token")).thenReturn("user@example.com");

        ResponseEntity<String> response = controller.protectedRoute(token);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Access granted"));
    }

    @Test
    void testProtectedRoute_invalidToken() {
        String token = "Bearer invalid.token";

        when(tokenService.validateToken("invalid.token")).thenReturn(false);

        ResponseEntity<String> response = controller.protectedRoute(token);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid token", response.getBody());
    }

    @Test
    void testCreateUser_manual() {
        User user = new User();
        user.setName("Ana");
        user.setEmail("ana@example.com");
        user.setPassword("1234");

        when(userService.createUser("Ana", "ana@example.com", "1234")).thenReturn(user);

        User result = controller.createUser(user);

        assertNotNull(result);
        assertEquals("Ana", result.getName());
    }

    @Test
    void testCreateUser_google() {
        User user = new User();
        user.setName("Ana");
        user.setEmail("ana@example.com");
        user.setPassword(null); // Google

        when(userService.createUser("Ana", "ana@example.com", null)).thenReturn(user);

        User result = controller.createUser(user);

        assertNotNull(result);
        assertEquals("ana@example.com", result.getEmail());
    }

    @Test
    void testLogin_success() {
        User login = new User();
        login.setEmail("ana@example.com");
        login.setPassword("1234");

        when(userService.validateUser("ana@example.com", "1234")).thenReturn(true);

        boolean result = controller.login(login);

        assertTrue(result);
    }

    @Test
    void testGetUserByEmail_found() {
        User user = new User();
        user.setEmail("ana@example.com");

        when(userService.getUserByEmail("ana@example.com")).thenReturn(user);

        ResponseEntity<User> response = controller.getUserByEmail("ana@example.com");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ana@example.com", response.getBody().getEmail());
    }

    @Test
    void testGetUserGroups() {
        Group group = new Group();
        group.setName("Viaje a Roma");

        when(userService.getUserGroups("ana@example.com")).thenReturn(List.of(group));

        List<Group> groups = controller.getUserGroups("ana@example.com");

        assertEquals(1, groups.size());
        assertEquals("Viaje a Roma", groups.get(0).getName());
    }

    @Test
    void testUpdateUser_success() {
        User updated = new User();
        updated.setEmail("ana@example.com");
        updated.setName("Ana Actualizada");
        updated.setAboutMe("Me gusta viajar");
        updated.setGender("F");
        updated.setAge(30);

        User existing = new User();
        existing.setEmail("ana@example.com");

        when(userService.getUserByEmail("ana@example.com")).thenReturn(existing);
        when(userService.saveUser(existing)).thenReturn(updated);

        ResponseEntity<User> response = controller.updateUser(updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Ana Actualizada", response.getBody().getName());
    }

    @Test
    void testCreateUserFromGoogle_success() throws Exception {
        // Arrange
        Map<String, String> payload = Map.of("token", "fakeToken");

        FirebaseToken mockedToken = mock(FirebaseToken.class);
        when(mockedToken.getEmail()).thenReturn("test@example.com");
        when(mockedToken.getName()).thenReturn("Test User");

        User mockedUser = new User("Test User", "test@example.com", null);
        when(userService.checkUserExistsByEmail("test@example.com")).thenReturn(false);
        when(userService.createUser("Test User", "test@example.com")).thenReturn(mockedUser);
        when(tokenService.generateToken("test@example.com")).thenReturn("jwt-token");

        try (MockedStatic<FirebaseAuth> firebaseAuthMockedStatic = mockStatic(FirebaseAuth.class)) {
            FirebaseAuth mockFirebaseAuth = mock(FirebaseAuth.class);
            firebaseAuthMockedStatic.when(FirebaseAuth::getInstance).thenReturn(mockFirebaseAuth);
            when(mockFirebaseAuth.verifyIdToken("fakeToken")).thenReturn(mockedToken);

            // Act
            ResponseEntity<?> response = controller.createUserFromGoogle(payload);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            Map<String, String> body = (Map<String, String>) response.getBody();
            assertEquals("test@example.com", body.get("email"));
            assertEquals("jwt-token", body.get("token"));
        }
    }


    @Test
    void testLoginWithGoogle_success() {
        Map<String, String> request = Map.of("email", "user@example.com");
        Map<String, Object> loginResponse = Map.of("token", "jwtToken");

        when(userService.handleGoogleLogin("user@example.com")).thenReturn(loginResponse);

        ResponseEntity<?> response = controller.loginWithGoogle(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("jwtToken", ((Map<?, ?>) response.getBody()).get("token"));
    }

    @Test
    void testCreateUser_incomplete() {
        User user = new User(); // sin nombre ni email
        User result = controller.createUser(user);
        assertNull(result);
    }

    @Test
    void testLogin_exception() {
        User login = new User();
        login.setEmail("crash@example.com");
        login.setPassword("1234");

        when(userService.validateUser(anyString(), anyString())).thenThrow(new RuntimeException("DB down"));

        boolean result = controller.login(login);
        assertFalse(result);
    }

    @Test
    void testGetUserByEmail_notFound() {
        when(userService.getUserByEmail("missing@example.com")).thenReturn(null);

        ResponseEntity<User> response = controller.getUserByEmail("missing@example.com");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdateUser_notFound() {
        User input = new User();
        input.setEmail("missing@example.com");

        when(userService.getUserByEmail("missing@example.com")).thenReturn(null);

        ResponseEntity<User> response = controller.updateUser(input);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testLoginWithGoogle_exception() {
        Map<String, String> request = Map.of("email", "fail@example.com");

        when(userService.handleGoogleLogin("fail@example.com")).thenThrow(new RuntimeException("Firebase error"));

        ResponseEntity<?> response = controller.loginWithGoogle(request);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error during Google login", response.getBody());
    }

    @Test
    void testCreateUserFromGoogle_existingUser() throws Exception {
        Map<String, String> payload = Map.of("token", "token");

        FirebaseToken firebaseToken = mock(FirebaseToken.class);
        when(firebaseToken.getEmail()).thenReturn("existing@example.com");
        when(firebaseToken.getName()).thenReturn("Existing User");

        User user = new User();
        user.setEmail("existing@example.com");

        try (MockedStatic<FirebaseAuth> firebaseAuthMock = mockStatic(FirebaseAuth.class)) {
            FirebaseAuth firebaseAuth = mock(FirebaseAuth.class);
            firebaseAuthMock.when(FirebaseAuth::getInstance).thenReturn(firebaseAuth);
            when(firebaseAuth.verifyIdToken("token")).thenReturn(firebaseToken);

            // Muy importante: asegurar mocks de usuario
            when(userService.checkUserExistsByEmail("existing@example.com")).thenReturn(true);
            when(userService.getUserByEmail("existing@example.com")).thenReturn(user);
            when(tokenService.generateToken("existing@example.com")).thenReturn("jwt");

            // Ejecutar
            ResponseEntity<?> response = controller.createUserFromGoogle(payload);

            // Verificar
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            Map<?, ?> body = (Map<?, ?>) response.getBody();
            assertEquals("jwt", body.get("token"));
            assertEquals("existing@example.com", body.get("email"));
        }
    }

    @Test
    void testProtectedRoute_malformedToken() {
        String token = "invalidFormat";

        ResponseEntity<String> response = controller.protectedRoute(token);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid token", response.getBody()); // Cambiado aquí
    }


    @Test
    void testCreateUserFromGoogle_firebaseException() throws Exception {
        Map<String, String> payload = Map.of("token", "badToken");

        try (MockedStatic<FirebaseAuth> firebaseAuthMock = mockStatic(FirebaseAuth.class)) {
            FirebaseAuth mockFirebaseAuth = mock(FirebaseAuth.class);
            firebaseAuthMock.when(FirebaseAuth::getInstance).thenReturn(mockFirebaseAuth);
            when(mockFirebaseAuth.verifyIdToken("badToken")).thenThrow(new RuntimeException("Firebase error"));

            ResponseEntity<?> response = controller.createUserFromGoogle(payload);

            assertEquals(500, response.getStatusCodeValue());
            assertTrue(((Map<?, ?>) response.getBody()).get("message").toString().contains("Firebase error"));
        }
    }

    @Test
    void testUpdateUser_nullFields() {
        User input = new User();
        input.setEmail("ana@example.com");

        User existing = new User();
        existing.setEmail("ana@example.com");

        when(userService.getUserByEmail("ana@example.com")).thenReturn(existing);
        when(userService.saveUser(existing)).thenReturn(existing);

        ResponseEntity<User> response = controller.updateUser(input);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ana@example.com", response.getBody().getEmail());
    }

    @Test
    void testGetUserGroups_empty() {
        when(userService.getUserGroups("ana@example.com")).thenReturn(Collections.emptyList());

        List<Group> groups = controller.getUserGroups("ana@example.com");

        assertNotNull(groups);
        assertTrue(groups.isEmpty());
    }

    @Test
    void testCreateUserFromGoogle_firebaseException_2() throws Exception {
        Map<String, String> payload = Map.of("token", "badToken");

        try (MockedStatic<FirebaseAuth> firebaseAuthMock = mockStatic(FirebaseAuth.class)) {
            FirebaseAuth mockAuth = mock(FirebaseAuth.class);
            firebaseAuthMock.when(FirebaseAuth::getInstance).thenReturn(mockAuth);

            when(mockAuth.verifyIdToken("badToken")).thenThrow(new RuntimeException("Firebase broken"));

            ResponseEntity<?> response = controller.createUserFromGoogle(payload);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertTrue(response.getBody().toString().contains("Google login/signup failed"));
        }
    }

    @Test
    void testUpdateUser_saveException() {
        User updated = new User();
        updated.setEmail("ana@example.com");
        updated.setName("Ana Nueva");

        User existing = new User();
        existing.setEmail("ana@example.com");

        when(userService.getUserByEmail("ana@example.com")).thenReturn(existing);
        when(userService.saveUser(existing)).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<User> response = controller.updateUser(updated);

        assertEquals(500, response.getStatusCodeValue());
    }
}
