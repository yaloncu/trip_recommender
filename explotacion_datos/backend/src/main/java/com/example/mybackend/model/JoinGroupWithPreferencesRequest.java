package com.example.mybackend.model;

import java.time.LocalDate;
import java.util.List;

public class JoinGroupWithPreferencesRequest {
    private String groupName;
    private String email;
    private String preference;
    private List<LocalDate> availabilityStartDates;
    private List<LocalDate> availabilityEndDates;

    // Getters y Setters
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupId) {
        this.groupName = groupId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getpreference() {
        return preference;
    }

    public void setpreference(String preference) {
        this.preference = preference;
    }

    public List<LocalDate> getAvailabilityStartDates(){
        return availabilityStartDates;
    }

    public void setAvailabilityStartDates(List<LocalDate> availabilityDates) {
        this.availabilityStartDates = availabilityDates;
    }

    public List<LocalDate> getAvailabilityEndDates(){
        return availabilityEndDates;
    }

    public void setAvailabilityEndDates(List<LocalDate> availabilityDates) {
        this.availabilityEndDates = availabilityDates;
    }
}

