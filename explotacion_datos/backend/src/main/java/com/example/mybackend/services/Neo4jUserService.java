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
import com.example.mybackend.model.Group;

/**
 * Neo4jUserService is a service that provides user-related operations using Neo4j.
 * It allows creating users, adding users to groups, and validating user credentials.
 */
@Service
public class Neo4jUserService {

    private final Driver driver;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public Neo4jUserService(Driver driver, PasswordEncoder passwordEncoder) {
        this.driver = driver;
        this.passwordEncoder = passwordEncoder;
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
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("CREATE (u:User {name: $name, email: $email, password: $password}) RETURN u",
                        Map.of("name", name, "email", email, "password", password));
                return null;
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a user with the given email to the group with the given name.
     *
     * @param email the email of the user
     * @param groupName the name of the group
     */
    public void createUser(String name, String email) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("CREATE (u:User {name: $name, email: $email}) RETURN u",
                        Map.of("name", name, "email", email));
                return null;
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a user with the given email to the group with the given name.
     *
     * @param email the email of the user
     * @param groupName the name of the group
     */
    public void addUserToGroup(String email, String groupName) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("MATCH (u:User {email: $email}) " +
                       "MATCH (g:Group {name: $groupName}) " +
                       "MERGE (u)-[:PERTENECE_A]->(g)",
                       Map.of("email", email, "groupName", groupName));
                return null;
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validate the user with the given email and password.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return true if the user is valid, false otherwise
     */
    public boolean validateUser(String email, String password){
        try (Session session = driver.session()){
            return session.executeRead(tx ->{
                Result result = tx.run("MATCH (u:User {email: $email, password: $password}) RETURN COUNT(u) > 0 AS userExists",
                Map.of("email", email, "password", password));
                return result.single().get("userExists").asBoolean();
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if a user with the given email exists.
     *
     * @param email the email of the user
     * @return true if the user exists, false otherwise
     */
    public boolean checkUserExistsByEmail(String email) {
        try (Session session = driver.session()){
            return session.executeRead(tx ->{
                Result result = tx.run("MATCH (u:User {email: $email}) RETURN COUNT(u) > 0 AS userExists",
                Map.of("email", email));
                return result.single().get("userExists").asBoolean();
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get the user with the given email.
     *
     * @param email the email of the user
     * @return the user with the given email, or null if the user does not exist
     */
    public User getUserByEmail(String email) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("MATCH (u:User {email: $email}) RETURN u",
                                        Map.of("email", email));
                if (result.hasNext()) {
                    var record = result.next();
                    var node = record.get("u").asNode();
                    return new User(
                        node.get("name").asString(),
                        node.get("email").asString(),
                        node.get("password").asString()
                    );
                } else {
                    return null;
                }
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the groups that the user with the given email belongs to.
     *
     * @param email the email of the user
     * @return a list of groups that the user belongs to
     */
    public List<Group> getUserGroups(String email) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run(
                    "MATCH (u:User {email: $email})-[:PERTENECE_A]->(g:Group) RETURN g",
                    Map.of("email", email)
                );

                List<Group> groups = new ArrayList<>();
                while (result.hasNext()) {
                    var record = result.next();
                    var node = record.get("g").asNode();
                    Group group = new Group(
                        node.get("name").asString()
                    );
                    groups.add(group);
                }
                return groups;
            });
        } catch (Neo4jException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}