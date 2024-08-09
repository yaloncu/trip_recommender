package com.example.mybackend.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;

    @Relationship(type = "PERTENECE_A", direction = Relationship.Direction.OUTGOING)
    private List<Group> groups = new ArrayList<>(); 

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    // Método auxiliar para añadir un grupo
    public void addGroup(Group group) {
        this.groups.add(group);
    }
}