package com.example.mybackend.model;

import lombok.AllArgsConstructor;


public class VoteRequest {
    private Long groupId;
    private String userId;
    private String city;

    public VoteRequest() {}

    public VoteRequest(Long groupId, String userId, String city) {
        this.groupId = groupId;
        this.userId = userId;
        this.city = city;
    }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}