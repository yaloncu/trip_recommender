package com.example.mybackend.controller.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybackend.controller.UserController;
import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.services.Neo4jUserService;
import com.example.mybackend.services.TokenService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

@RestController
@RequestMapping("/api")
public class UserControllerImpl implements UserController {
    
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
    public User createUser(@RequestBody User user) {
        try {
            if (user.getName() == null || user.getEmail() == null) {
                return null;
            }

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return userService.createUser(user.getName(), user.getEmail(), null); // Registro desde Google
            } else {
                return userService.createUser(user.getName(), user.getEmail(), user.getPassword()); // Registro manual
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create a new user from Google.
     *
     * @param payload the user data from Google
     * @return a response indicating the creation status
     */
    @PostMapping("/signup/google")
    public ResponseEntity<?> createUserFromGoogle(@RequestBody Map<String, String> payload) {
        try {
            String firebaseToken = payload.get("token");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();

            User user;
            if (!userService.checkUserExistsByEmail(email)) {
                user = userService.createUser(name, email);
            } else {
                user = userService.getUserByEmail(email);
            }

            String jwt = tokenService.generateToken(user.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("email", user.getEmail());
            response.put("token", jwt);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Google login/signup failed: " + e.getMessage()));
        }
    }


    /**
     * Login a user.
     *
     * @param loginRequest the login request containing email and password
     * @return a response indicating the login status
     */
    @PostMapping("/login")
    public boolean login(@RequestBody User loginRequest) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();
            return userService.validateUser(email, password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Login a user with Google.
     *
     * @param googleLoginRequest the login request containing email
     * @return a response indicating the login status
     */
    @PostMapping("/login/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> googleLoginRequest) {
        try {
            String email = googleLoginRequest.get("email");
            Map<String, Object> response = userService.handleGoogleLogin(email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during Google login");
        }
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Get groups for a specific user.
     *
     * @param email the email of the user
     * @return a list of groups the user belongs to
     */
    @GetMapping("/users/{email}/groups")
    public List<Group> getUserGroups(@PathVariable String email) {
        try {
            return userService.getUserGroups(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/users/update")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        try {
            User existingUser = userService.getUserByEmail(updatedUser.getEmail());
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            existingUser.setName(updatedUser.getName());
            existingUser.setAboutMe(updatedUser.getAboutMe());
            existingUser.setGender(updatedUser.getGender());
            existingUser.setAge(updatedUser.getAge());

            User savedUser = userService.saveUser(existingUser);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
