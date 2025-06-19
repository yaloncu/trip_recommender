package com.example.mybackend.model;

import com.google.auto.value.AutoValue.Builder;


@Builder
public record SimpleUserDTO(
    String name,
    String email,
    String aboutMe,
    String gender,
    Integer age
) {}

