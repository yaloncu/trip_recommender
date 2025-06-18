package com.example.mybackend.model;

public record SimpleUserDTO(
    String name,
    String email,
    String aboutMe,
    String gender,
    Integer age
) {}

