package com.example.mybackend.model;

import java.util.List;

public class JoinGroupWithPreferencesRequest {
    private Long groupId;
    private String email;
    private String preference;

    // Getters y Setters
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

