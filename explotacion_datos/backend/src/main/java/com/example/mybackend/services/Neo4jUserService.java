package com.example.mybackend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Result;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mybackend.model.User;
import com.example.mybackend.repository.UserRepository;
import com.example.mybackend.model.Group;

/**
 * Neo4jUserService is a service that provides user-related operations using Neo4j.
 * It allows creating users, adding users to groups, and validating user credentials.
 */

public interface Neo4jUserService {

    /**
     * Create a new user with the given name, email, and password.
     *
     * @param name the name of the user
     * @param email the email of the user
     * @param password the password of the user
     */
    User createUser(String name, String email, String password);

    /**
     * Add a user with the given email to the group with the given name.
     *
     * @param email the email of the user
     * @param groupName the name of the group
     */
    User createUser(String name, String email);

    /**
     * Add a user with the given email to the group with the given name.
     *
     * @param email the email of the user
     * @param groupName the name of the group
     */
    void addUserToGroup(String email, String groupName);

    /**
     * Validate the user with the given email and password.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return true if the user is valid, false otherwise
     */
    boolean validateUser(String email, String password);
    
    public Map<String, Object> loginWithGoogle(String email);

    /**
     * Check if a user with the given email exists.
     *
     * @param email the email of the user
     * @return true if the user exists, false otherwise
     */
    boolean checkUserExistsByEmail(String email);
    
    /**
     * Get the user with the given email.
     *
     * @param email the email of the user
     * @return the user with the given email, or null if the user does not exist
     */
    User getUserByEmail(String email);

    /**
     * Get the groups that the user with the given email belongs to.
     *
     * @param email the email of the user
     * @return a list of groups that the user belongs to
     */
    List<Group> getUserGroups(String email);
}