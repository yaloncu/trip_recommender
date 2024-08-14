package com.example.mybackend.services;

import com.example.mybackend.model.Group;
import com.example.mybackend.model.User;
import com.example.mybackend.config.Neo4jConnection;
import com.example.mybackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import java.util.Map;

public class UserService {

    private final Neo4jConnection neo4jConnection;

    public UserService(Neo4jConnection neo4jConnection) {
        this.neo4jConnection = neo4jConnection;
    }

    public void createUser(String name, String email, String password) {
        try (Session session = neo4jConnection.getDriver().session()) {
            session.run("CREATE (u:User {name: $name, email: $email, password: $password})",
                    Map.of("name", name, "email", email, "password", password));
        }
    }

    public void close() {
        neo4jConnection.close();
    }
}