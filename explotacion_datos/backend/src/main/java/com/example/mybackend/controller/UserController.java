package com.example.mybackend.controller;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.services.GroupService;
import com.example.mybackend.services.Neo4jUserService;
import com.example.mybackend.services.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * UserController is a REST controller that handles HTTP requests related to users.
 * It provides endpoints to create users, login, and get user groups.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private Neo4jUserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * Access a protected route.
     *
     * @param token the authorization token
     * @return a response indicating the access status
     */
    @GetMapping("/protected-route")
    public ResponseEntity<String> protectedRoute(@RequestHeader("Authorization") String token) {
        try {
            // Elimina el prefijo "Bearer " del token si existe
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            if (tokenService.validateToken(token)) {
                String email = tokenService.extractEmailFromToken(token);
                return ResponseEntity.ok("Access granted for " + email);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error validating token");
        }
    }
    
    /**
     * Create a new user.
     *
     * @param user the user to create
     * @return a response indicating the creation status
     */
    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            if (user.getName() == null || user.getEmail() == null) {
                return ResponseEntity.badRequest().body("Name and Email are required.");
            }

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                userService.createUser(user.getName(), user.getEmail(), null); // Registro desde Google
            } else {
                userService.createUser(user.getName(), user.getEmail(), user.getPassword()); // Registro manual
            }

            return ResponseEntity.ok("User created successfully: " + user.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
        }
    }

    /**
     * Create a new user from Google.
     *
     * @param payload the user data from Google
     * @return a response indicating the creation status
     */
    @PostMapping("/signup/google")
    public ResponseEntity<String> createUserFromGoogle(@RequestBody Map<String, String> payload) {
        try {
            String name = payload.get("name");
            String email = payload.get("email");

            if (name == null || email == null) {
                return ResponseEntity.badRequest().body("Name and Email are required.");
            }

            userService.createUser(name, email);
            return ResponseEntity.ok("Google user created successfully: " + name);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating Google user: " + e.getMessage());
        }
    }

    /**
     * Login a user.
     *
     * @param loginRequest the login request containing email and password
     * @return a response indicating the login status
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User loginRequest) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();
            boolean isValid = userService.validateUser(email, password);

            if (isValid) {
                String token = tokenService.generateToken(email);
                return ResponseEntity.ok(Map.of("message", "Login successful", "token", token, "email", email));
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Error during login"));
        }
    }

    /**
     * Login a user with Google.
     *
     * @param googleLoginRequest the login request containing email
     * @return a response indicating the login status
     */
    @PostMapping("/login/google")
    public ResponseEntity<Map<String, String>> loginWithGoogle(@RequestBody Map<String, String> googleLoginRequest) {
        try {
            String email = googleLoginRequest.get("email");

            if (userService.checkUserExistsByEmail(email)) {
                String token = tokenService.generateToken(email);
                return ResponseEntity.ok(Map.of("message", "Google login successful", "token", token, "email", email));
            } else {
                return ResponseEntity.status(404).body(Map.of("message", "Google user not found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Error during Google login"));
        }
    }

    @GetMapping("/users/{email}/groups")
    public ResponseEntity<List<Group>> getUserGroups(@PathVariable String email) {
        try {
            List<Group> groups = userService.getUserGroups(email);

            if (groups != null && !groups.isEmpty()) {
                return ResponseEntity.ok(groups);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
