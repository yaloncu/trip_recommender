package com.example.mybackend.model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JoinGroupWithPreferencesRequest represents a request to join a group with preferences.
 */
@Builder
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class JoinGroupWithPreferencesRequest {
    private String groupName;
    private String email;
    private String preference;
    private List<LocalDate> availabilityStartDates;
    private List<LocalDate> availabilityEndDates;

    // Getters y Setters
    /**
     * Gets the name of the group.
     *
     * @return the name of the group
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the name of the group.
     *
     * @param groupId the name of the group
     */
    public void setGroupName(String groupId) {
        this.groupName = groupId;
    }

    /**
     * Gets the email of the user.
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the preference of the user.
     *
     * @return the preference of the user
     */
    public String getpreference() {
        return preference;
    }

    /**
     * Sets the preference of the user.
     *
     * @param preference the preference of the user
     */
    public void setpreference(String preference) {
        this.preference = preference;
    }

    /**
     * Gets the availability start dates of the user.
     *
     * @return the availability start dates of the user
     */
    public List<LocalDate> getAvailabilityStartDates(){
        return availabilityStartDates;
    }

    /**
     * Sets the availability start dates of the user.
     *
     * @param availabilityDates the availability start dates of the user
     */
    public void setAvailabilityStartDates(List<LocalDate> availabilityDates) {
        this.availabilityStartDates = availabilityDates;
    }

    /**
     * Gets the availability end dates of the user.
     *
     * @return the availability end dates of the user
     */
    public List<LocalDate> getAvailabilityEndDates(){
        return availabilityEndDates;
    }

    /**
     * Sets the availability end dates of the user.
     *
     * @param availabilityDates the availability end dates of the user
     */
    public void setAvailabilityEndDates(List<LocalDate> availabilityDates) {
        this.availabilityEndDates = availabilityDates;
    }
}

