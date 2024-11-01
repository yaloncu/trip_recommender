package com.example.mybackend.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Node
public class Group {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String privated;
    private String audience;
    private String email;
    private boolean isClosed;
    private boolean isClosedVoting;
    private String type;
    private LocalDate departureDate;
    private LocalDate returnDate;

    @Relationship(type = "PERTENECE_A", direction = Relationship.Direction.INCOMING)
    private List<User> users = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }
    public Group() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrivated(){
        return privated;
    }

    public void setPrivated(String privated){
        this.privated = privated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosedVoting(boolean isClosed) {
        this.isClosedVoting = isClosed;
    }

    public boolean isClosedVoting() {
        return isClosedVoting;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }
    
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type; 
    }

    public void setType(String type) {
        this.type = type; 
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate deparDate) {
        this.departureDate = deparDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate retrunDate) {
        this.returnDate = retrunDate;
    }
    
    public void addUser(User user) {
        this.users.add(user);
    }
}