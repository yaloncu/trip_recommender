package com.example.mybackend.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Group represents a group entity in the application.
 * It contains information about the group such as its name, members, and other relevant details.
 */

@Node
@Builder
@Data 
@NoArgsConstructor
@AllArgsConstructor
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private List<LocalDate> availabilityStartDates;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private List<LocalDate> availabilityEndDates;

    @Relationship(type = "PERTENECE_A", direction = Relationship.Direction.INCOMING)
    private List<User> users = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

    // Getters y Setters
    /**
     * Gets the ID of the group.
     *
     * @return the ID of the group
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the group.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the privacity of the group.
     *
     * @return the privacity of the group
     */
    public String getPrivated(){
        return privated;
    }

    /**
     * Sets the privacity of the group.
     *
     * @param privated the privacity to set
     */
    public void setPrivated(String privated){
        this.privated = privated;
    }

    /**
     * Gets the name of the group.
     *
     * @return the name of the group
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the group.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the audience of the group.
     *
     * @return the audience of the group
     */
    public String getAudience() {
        return audience;
    }

    /**
     * Sets the audience of the group.
     *
     * @param audience the audience to set
     */
    public void setAudience(String audience) {
        this.audience = audience;
    }

    /**
     * Gets the closed status of the group.
     *
     * @return the closed status of the group
     */
    public boolean isClosed() {
        return isClosed;
    }

    /** 
     * Sets the closed status of the group.
     *
     * @param isClosed the closed status to set
     */
     public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }
    
    /**
     * Gets the closed voting status of the group.
     *
     * @return the closed voting status of the group
     */
    public boolean isClosedVoting() {
        return isClosedVoting;
    }

    /**
     * Sets the closed voting status of the group.
     *
     * @param isClosed the closed voting status to set
     */
    public void setClosedVoting(boolean isClosed) {
        this.isClosedVoting = isClosed;
    }

    /**
     * Gets the users of the group.
     *
     * @return the users of the group
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Sets the users of the group.
     *
     * @param users the users to set
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * Gets the email of the group. 
     * 
     * @return the email of the group
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the group.
     * 
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the type of the group.
     * 
     * @return the type of the group
     */
    public String getType() {
        return type; 
    }

    /**
     * Sets the type of the group.
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type; 
    }

    /**
     * Gets the departure date of the group.
     * 
     * @return the departure date of the group
     */
    public LocalDate getDepartureDate() {
        return departureDate;
    }

    /**
     * Sets the departure date of the group.
     * 
     * @param deparDate the departure date to set
     */
    public void setDepartureDate(LocalDate deparDate) {
        this.departureDate = deparDate;
    }

    /**
     * Gets the return date of the group.
     * 
     * @return the return date of the group
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Sets the return date of the group.
     * 
     * @param retrunDate the return date to set
     */
    public void setReturnDate(LocalDate retrunDate) {
        this.returnDate = retrunDate;
    }

    /**
     * Gets the availability start dates of the group.
     * 
     * @return the availability start dates of the group
     */
    public List<LocalDate> getAvailabilityStartDates() {
        return availabilityStartDates;
    }

    /**
     * Sets the availability start dates of the group.
     * 
     * @param startDate the availability start dates to set
     */
    public void setAvailabilityStartDates(List<LocalDate> startDate) {
        this.availabilityStartDates = startDate;
    }

    /**
     * Gets the availability end dates of the group.
     * 
     * @return the availability end dates of the group
     */
    public List<LocalDate> getAvailabilityEndDates() {
        return availabilityEndDates;
    }

    /**
     * Sets the availability end dates of the group.
     * 
     * @param endDate the availability end dates to set
     */
    public void setAvailabilityEndDates(List<LocalDate> endDate) {
        this.availabilityEndDates = endDate;
    }
    
    /**
     * Adds a user to the group.
     *
     * @param user the user to add
     */
    public void addUser(User user) {
        this.users.add(user);
    }
}