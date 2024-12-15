package com.example.mybackend.controller;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.services.GroupService;
import com.example.mybackend.services.Neo4jUserService;
import com.example.mybackend.services.UserService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private Neo4jUserService userService;
    @Autowired
    private GroupService groupService;

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            if (user.getName() == null || user.getEmail() == null) {
                return ResponseEntity.badRequest().body("Name and Email are required.");
            }

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                // Registro desde Google (sin contraseña)
                userService.createUser(user.getName(), user.getEmail(), null);
            } else {
                // Registro manual (con contraseña)
                userService.createUser(user.getName(), user.getEmail(), user.getPassword());
            }

            return ResponseEntity.ok("User created successfully: " + user.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
    }

    @PostMapping("/signup/google")
    public ResponseEntity<String> createUserFromGoogle(@RequestBody Map<String, String> payload) {
        try {
            String name = payload.get("name");
            String email = payload.get("email");

            if (name == null || email == null) {
                throw new IllegalArgumentException("Name or email is missing");
            }

            userService.createUser(name, email);
            return ResponseEntity.ok("Google user created successfully: " + name);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Google user: " + e.getMessage());
        }
    }


    @RequestMapping(value = "/api/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptionsRequest() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        boolean isValid = userService.validateUser(email, password);
        if (isValid) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/login/google")
    public ResponseEntity<String> loginWithGoogle(@RequestBody Map<String, String> googleLoginRequest) {
        String email = googleLoginRequest.get("email");
        if (userService.checkUserExistsByEmail(email)) {
            return ResponseEntity.ok("Google login successful");
        } else {
            return ResponseEntity.status(404).body("Google user not found");
        }
    }

    

    @GetMapping("/users/{email}/groups")
    public ResponseEntity<List<Group>> getUserGroups(@PathVariable String email) {
        List<Group> groups = userService.getUserGroups(email);
        if (!groups.isEmpty()) {
            return ResponseEntity.ok(groups);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<String> callback(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        User existingUser = userService.getUserByEmail(email);
        if (existingUser == null) {
            // sign in
            userService.createUser(name, email, null); // null para password porque es login con Google
            return ResponseEntity.ok("Usuario registrado exitosamente con Google: " + email);
        } else {
            // log in
            return ResponseEntity.ok("Inicio de sesión exitoso para: " + email);
        }
    }

}