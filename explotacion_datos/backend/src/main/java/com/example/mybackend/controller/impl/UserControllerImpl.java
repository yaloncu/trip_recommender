package com.example.mybackend.controller.impl;

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
    public User createUserFromGoogle(@RequestBody Map<String, String> payload) {
        try {
            String name = payload.get("name");
            String email = payload.get("email");

            if (name == null || email == null) {
                return null;
            }
            if (userService.checkUserExistsByEmail(email)) {
                return userService.getUserByEmail(email);
            } else {
                return userService.createUser(name, email, null); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            boolean exists = userService.checkUserExistsByEmail(email);

            if (exists) {
                return ResponseEntity.ok().body("User exists");
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
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
}
