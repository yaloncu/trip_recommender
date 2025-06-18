package com.example.mybackend.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.repository.UserRepository;
import com.example.mybackend.services.Neo4jUserService;
import com.example.mybackend.services.TokenService;
@Service
public class Neo4jUserServiceImpl implements Neo4jUserService {

    private final Driver driver;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService; 
    
    public Neo4jUserServiceImpl(Driver driver, PasswordEncoder passwordEncoder, UserRepository userRepository, TokenService tokenService) {
        this.driver = driver;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository; 
        this.tokenService = tokenService; 
    }

    /**
     * Create a new user with the given name, email, and password.
     *
     * @param name the name of the user
     * @param email the email of the user
     * @param password the password of the user
     */
    public User createUser(String name, String email, String password) {
        if (password == null) {
            return userRepository.createUser(name, email); // llama a la versión de dos parámetros
        }
        String hashedPassword = passwordEncoder.encode(password);
        return userRepository.createUser(name, email, hashedPassword);
    }

    /**
     * Add a user with the given email to the group with the given name.
     *
     * @param email the email of the user
     * @param groupName the name of the group
     */
    public User createUser(String name, String email) {
        return userRepository.createUser(name, email);
    }

    public Map<String, Object> loginWithGoogle(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        String token = tokenService.generateToken(user.getEmail()); 
        return Map.of("user", user, "token", token);
    }

    public Map<String, Object> handleGoogleLogin(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User does not exist");
        }

        String token = tokenService.generateToken(user.getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        return response;
    }

    /**
     * Add a user with the given email to the group with the given name.
     *
     * @param email the email of the user
     * @param groupName the name of the group
     */
    public void addUserToGroup(String email, String groupName) {
        userRepository.addUserToGroup(email, groupName);
    }

    /**
     * Validate the user with the given email and password.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return true if the user is valid, false otherwise
     */
    public boolean validateUser(String email, String password){
        return userRepository.validateUser(email, password);
    }
    
    /**
     * Check if a user with the given email exists.
     *
     * @param email the email of the user
     * @return true if the user exists, false otherwise
     */
    public boolean checkUserExistsByEmail(String email) {
        return userRepository.checkUserExists(email);
    }
    
    /**
     * Get the user with the given email.
     *
     * @param email the email of the user
     * @return the user with the given email, or null if the user does not exist
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Get the groups that the user with the given email belongs to.
     *
     * @param email the email of the user
     * @return a list of groups that the user belongs to
     */
    public List<Group> getUserGroups(String email) {
        return userRepository.findUserGroups(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    
}
