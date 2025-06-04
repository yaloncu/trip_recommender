package com.example.mybackend.services.impl;

import java.util.ArrayList;
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
@Service
public class Neo4jUserServiceImpl implements Neo4jUserService {

    private final Driver driver;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    
    @Autowired
    public Neo4jUserServiceImpl(Driver driver, PasswordEncoder passwordEncoder) {
        this.driver = driver;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = null; 
    }

    /**
     * Create a new user with the given name, email, and password.
     *
     * @param name the name of the user
     * @param email the email of the user
     * @param password the password of the user
     */
    public void createUser(String name, String email, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        userRepository.createUser(name, email, hashedPassword);
    }

    /**
     * Add a user with the given email to the group with the given name.
     *
     * @param email the email of the user
     * @param groupName the name of the group
     */
    public void createUser(String name, String email) {
        userRepository.createUser(name, email);
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
        return userRepository.findUserGroups(email).getGroups();
    }
    
}
