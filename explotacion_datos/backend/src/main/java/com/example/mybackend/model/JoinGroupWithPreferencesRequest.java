package com.example.mybackend.model;

import java.util.List;

public class JoinGroupWithPreferencesRequest {
    private String groupName;
    private String email;
    private String preference;

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
}

