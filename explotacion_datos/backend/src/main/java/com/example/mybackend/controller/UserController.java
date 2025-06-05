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
@RequestMapping("/api")
public interface UserController {

    /**
     * Access a protected route.
     *
     * @param token the authorization token
     * @return a response indicating the access status
     */
    @GetMapping("/protected-route")
    public ResponseEntity<String> protectedRoute(@RequestHeader("Authorization") String token);

    /**
     * Create a new user.
     *
     * @param user the user to create
     * @return a response indicating the creation status
     */
    @PostMapping("/signup")
    public User createUser(@RequestBody User user);

    /**
     * Create a new user from Google.
     *
     * @param payload the user data from Google
     * @return a response indicating the creation status
     */
    @PostMapping("/signup/google")
    public User createUserFromGoogle(@RequestBody Map<String, String> payload);

    /**
     * Login a user.
     *
     * @param loginRequest the login request containing email and password
     * @return a response indicating the login status
     */
    @PostMapping("/login")
    public boolean login(@RequestBody User loginRequest);

    /**
     * Login a user with Google.
     *
     * @param googleLoginRequest the login request containing email
     * @return a response indicating the login status
     */
    @PostMapping("/login/google")
    ResponseEntity<?> loginWithGoogle(Map<String, String> googleLoginRequest);

    /**
     * Get groups for a specific user.
     *
     * @param email the email of the user
     * @return a list of groups the user belongs to
     */
    @GetMapping("/users/{email}/groups")
    public List<Group> getUserGroups(@PathVariable String email);
}
