package com.example.mybackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinActivityRequest {
    private String email;
}