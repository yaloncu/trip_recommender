package com.example.mybackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

import com.google.auto.value.AutoValue.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityGroupDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String type;
    private String adminEmail;
    private ZonedDateTime startDateTime;
    private int numberOfParticipants;
}
