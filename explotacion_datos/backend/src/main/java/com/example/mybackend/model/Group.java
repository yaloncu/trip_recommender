package com.example.mybackend.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
public class Group {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Relationship(type = "PERTENECE_A", direction = Relationship.Direction.INCOMING)
    private List<User> users;

    // Getters y Setters

    // Método auxiliar para añadir un usuario
    public void addUser(User user) {
        this.users.add(user);
    }
}
