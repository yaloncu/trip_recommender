package com.example.mybackend.services;

import java.util.Map;

import com.example.mybackend.model.*;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class Neo4jUserService {

    private Driver driver;

    public void createUser(String name, String email, String password) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("CREATE (u:User {name: $name, email: $email, password: $password})",
                        Map.of("name", name, "email", email, "password", password));
                return null;
            });
        }
    }

    public void addUserToGroup(String email, String groupName) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("MATCH (u:User {email: $email}) " +
                       "MATCH (g:Group {name: $groupName}) " +
                       "MERGE (u)-[:PERTENECE_A]->(g)",
                       Map.of("email", email, "groupName", groupName));
                return null;
            });
        }
    }
    public User getUserByEmail(String email) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("MATCH (u:User {email: $email}) RETURN u",
                                        Map.of("email", email));
                if (result.hasNext()) {
                    var record = result.next();
                    return new User(record.get("u").asNode().get("name").asString(),
                                    record.get("u").asNode().get("email").asString(),
                                    record.get("u").asNode().get("password").asString());
                } else {
                    return null;
                }
            });
        }
    }
    
}
